const API = 'http://localhost:8080';
let token = localStorage.getItem('token');
let userEmail = localStorage.getItem('email');

function api(path, opts = {}) {
  const headers = { 'Content-Type': 'application/json' };
  if (token) headers['Authorization'] = `Bearer ${token}`;
  return fetch(`${API}${path}`, { ...opts, headers })
    .then(r => r.ok ? r.json().catch(() => null) : r.json().then(e => { throw e; }));
}

function showPage(id) {
  document.querySelectorAll('.page').forEach(p => p.classList.remove('active'));
  document.getElementById(id).classList.add('active');
}

function showTab(name) {
  document.querySelectorAll('.tab').forEach(t => t.classList.remove('active'));
  document.querySelectorAll('.tab-content').forEach(t => t.classList.remove('active'));
  document.querySelector(`[data-tab="${name}"]`).classList.add('active');
  document.getElementById(`tab-${name}`).classList.add('active');
}

// Auth
document.getElementById('toggle-login').onclick = () => {
  document.querySelectorAll('.toggle button').forEach(b => b.classList.remove('active'));
  document.getElementById('toggle-login').classList.add('active');
  document.querySelectorAll('.auth-form').forEach(f => f.classList.remove('active'));
  document.getElementById('login-form').classList.add('active');
};
document.getElementById('toggle-register').onclick = () => {
  document.querySelectorAll('.toggle button').forEach(b => b.classList.remove('active'));
  document.getElementById('toggle-register').classList.add('active');
  document.querySelectorAll('.auth-form').forEach(f => f.classList.remove('active'));
  document.getElementById('register-form').classList.add('active');
};

document.getElementById('login-form').onsubmit = async (e) => {
  e.preventDefault();
  try {
    const res = await api('/api/auth/login', {
      method: 'POST', body: JSON.stringify({
        email: document.getElementById('login-email').value,
        password: document.getElementById('login-password').value
      })
    });
    token = res.token; userEmail = res.email;
    localStorage.setItem('token', token);
    localStorage.setItem('email', userEmail);
    enterApp();
  } catch (err) { document.getElementById('login-error').textContent = err.message || 'Ошибка входа'; }
};

document.getElementById('register-form').onsubmit = async (e) => {
  e.preventDefault();
  try {
    const res = await api('/api/auth/register', {
      method: 'POST', body: JSON.stringify({
        username: document.getElementById('reg-username').value,
        email: document.getElementById('reg-email').value,
        password: document.getElementById('reg-password').value
      })
    });
    token = res.token; userEmail = res.email;
    localStorage.setItem('token', token);
    localStorage.setItem('email', userEmail);
    enterApp();
  } catch (err) { document.getElementById('reg-error').textContent = err.message || 'Ошибка регистрации'; }
};

document.getElementById('logout-btn').onclick = () => {
  token = null; userEmail = null;
  localStorage.removeItem('token'); localStorage.removeItem('email');
  showPage('auth-page');
};

function enterApp() {
  showPage('app-page');
  document.getElementById('user-email').textContent = userEmail;
  loadTodos(); loadNotes(); loadFinance(); loadEvents();
  loadDashboard(); loadModules();
}

// Tabs
document.querySelectorAll('.tab').forEach(tab => {
  tab.onclick = () => {
    showTab(tab.dataset.tab);
    if (tab.dataset.tab === 'todos') loadTodos();
    if (tab.dataset.tab === 'notes') loadNotes();
    if (tab.dataset.tab === 'finance') loadFinance();
    if (tab.dataset.tab === 'planner') loadEvents();
    if (tab.dataset.tab === 'dashboard') loadDashboard();
    if (tab.dataset.tab === 'modules') loadModules();
  };
});

// Todos
document.getElementById('todo-form').onsubmit = async (e) => {
  e.preventDefault();
  await api('/api/todos', { method: 'POST', body: JSON.stringify({
    title: document.getElementById('todo-title').value,
    priority: document.getElementById('todo-priority').value
  })});
  document.getElementById('todo-title').value = '';
  loadTodos();
};

async function loadTodos() {
  try {
    const items = await api('/api/todos');
    document.getElementById('todo-list').innerHTML = items.map(t => `
      <li>
        <div><span class="title">${t.title}</span> <span class="meta">${t.status || ''}</span></div>
        <span class="badge badge-${(t.priority||'').toLowerCase()}">${t.priority}</span>
      </li>`).join('');
  } catch {}
}

// Notes
document.getElementById('note-form').onsubmit = async (e) => {
  e.preventDefault();
  await api('/api/notes', { method: 'POST', body: JSON.stringify({
    title: document.getElementById('note-title').value,
    content: document.getElementById('note-content').value
  })});
  document.getElementById('note-title').value = '';
  document.getElementById('note-content').value = '';
  loadNotes();
};

async function loadNotes() {
  try {
    const items = await api('/api/notes');
    document.getElementById('note-list').innerHTML = items.map(n => `
      <li><div><span class="title">${n.title}</span><br><span class="meta">${n.content || ''}</span></div></li>`).join('');
  } catch {}
}

// Finance
document.getElementById('finance-form').onsubmit = async (e) => {
  e.preventDefault();
  await api('/api/finance/transactions', { method: 'POST', body: JSON.stringify({
    type: document.getElementById('finance-type').value,
    amount: parseFloat(document.getElementById('finance-amount').value),
    category: document.getElementById('finance-category').value
  })});
  document.getElementById('finance-amount').value = '';
  document.getElementById('finance-category').value = '';
  loadFinance();
};

async function loadFinance() {
  try {
    const items = await api('/api/finance/transactions');
    document.getElementById('finance-list').innerHTML = items.map(t => `
      <li>
        <div><span class="title">${t.category || ''}</span> <span class="meta">${t.description || ''}</span></div>
        <span class="badge badge-${(t.type||'').toLowerCase()}">${t.type} ${t.amount?.toFixed(2)}</span>
      </li>`).join('');
  } catch {}
  try {
    const bal = await api('/api/finance/balance');
    document.getElementById('finance-balance').textContent = `Баланс: ${bal.balance?.toFixed(2) || 0} ₽`;
  } catch {}
}

// Events
document.getElementById('event-form').onsubmit = async (e) => {
  e.preventDefault();
  const dt = document.getElementById('event-date').value;
  await api('/api/planner/events', { method: 'POST', body: JSON.stringify({
    title: document.getElementById('event-title').value,
    dateTime: new Date(dt).toISOString(),
    duration: 60
  })});
  document.getElementById('event-title').value = '';
  loadEvents();
};

async function loadEvents() {
  try {
    const items = await api('/api/planner/events');
    document.getElementById('event-list').innerHTML = items.map(e => `
      <li>
        <span class="title">${e.title}</span>
        <span class="meta">${e.dateTime ? new Date(e.dateTime).toLocaleString() : ''}</span>
      </li>`).join('');
  } catch {}
}

// Dashboard
async function loadDashboard() {
  try {
    const data = await api('/api/dashboard/summary');
    document.getElementById('dashboard-content').innerHTML = [
      { label: 'Задачи', value: data.todoCount ?? '—' },
      { label: 'Заметки', value: data.notesCount ?? '—' },
      { label: 'Баланс', value: data.balance ?? '—' },
      { label: 'События', value: data.eventsCount ?? '—' }
    ].map(c => `<div class="card"><h3>${c.label}</h3><div class="value">${c.value}</div></div>`).join('');
  } catch {
    document.getElementById('dashboard-content').innerHTML = '<p>Нет данных</p>';
  }
}

// Modules
async function loadModules() {
  try {
    const items = await api('/api/modules');
    document.getElementById('module-list').innerHTML = (items || []).map(m => `
      <li><span class="title">${m.name || m.moduleName || m.id || ''}</span></li>`).join('');
  } catch {}
}

// Init
if (token) { enterApp(); } else { showPage('auth-page'); }
