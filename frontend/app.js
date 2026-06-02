const API = 'http://localhost:8080';
let token = localStorage.getItem('token');
let userEmail = localStorage.getItem('email');
let modules = [];
let modulesCache = {};
let activeTab = 'dashboard';
let widgets = [];

function api(path, opts = {}) {
  const headers = { 'Content-Type': 'application/json' };
  if (token) headers['Authorization'] = `Bearer ${token}`;
  return fetch(`${API}${path}`, { ...opts, headers })
    .then(r => {
      if (r.status === 401) { logout(); throw new Error('Unauthorized'); }
      const ct = r.headers.get('content-type') || '';
      if (ct.includes('application/json')) return r.json().then(d => d);
      return r.text().then(t => { throw new Error(t || r.statusText); });
    })
    .catch(err => { if (err.message !== 'Unauthorized') console.error(path, err); throw err; });
}

function toast(msg) {
  let el = document.querySelector('.toast');
  if (!el) {
    el = document.createElement('div');
    el.className = 'toast';
    document.body.appendChild(el);
  }
  el.textContent = msg;
  el.classList.add('show');
  clearTimeout(el._t);
  el._t = setTimeout(() => el.classList.remove('show'), 2500);
}

// Auth
function logout() {
  token = null; userEmail = null;
  localStorage.removeItem('token'); localStorage.removeItem('email');
  document.getElementById('auth-page').classList.add('active');
  document.getElementById('app-page').classList.remove('active');
}

function enterApp() {
  document.getElementById('auth-page').classList.remove('active');
  document.getElementById('app-page').classList.add('active');
  document.getElementById('sidebar-user').textContent = userEmail;
  document.getElementById('page-title').textContent = 'Дашборд';
  activeTab = 'dashboard';
  loadSidebar();
  switchTab('dashboard');
}

document.getElementById('login-form').onsubmit = async (e) => {
  e.preventDefault();
  try {
    const res = await api('/api/auth/login', {
      method: 'POST',
      body: JSON.stringify({
        email: document.getElementById('login-email').value,
        password: document.getElementById('login-password').value
      })
    });
    token = res.token; userEmail = res.email;
    localStorage.setItem('token', token);
    localStorage.setItem('email', userEmail);
    enterApp();
  } catch (err) {
    document.getElementById('auth-error').textContent = err.message || 'Ошибка входа';
  }
};

document.getElementById('register-form').onsubmit = async (e) => {
  e.preventDefault();
  try {
    const res = await api('/api/auth/register', {
      method: 'POST',
      body: JSON.stringify({
        username: document.getElementById('reg-username').value,
        email: document.getElementById('reg-email').value,
        password: document.getElementById('reg-password').value
      })
    });
    token = res.token; userEmail = res.email;
    localStorage.setItem('token', token);
    localStorage.setItem('email', userEmail);
    enterApp();
  } catch (err) {
    document.getElementById('auth-error').textContent = err.message || 'Ошибка регистрации';
  }
};

document.querySelectorAll('.auth-tab').forEach(tab => {
  tab.onclick = () => {
    document.querySelectorAll('.auth-tab').forEach(t => t.classList.remove('active'));
    tab.classList.add('active');
    document.querySelectorAll('.auth-form').forEach(f => f.classList.remove('active'));
    document.getElementById(tab.dataset.auth + '-form').classList.add('active');
    document.getElementById('auth-error').textContent = '';
  };
});

document.getElementById('sidebar-logout').onclick = logout;

// Sidebar
document.getElementById('burger-btn').onclick = () => {
  document.getElementById('sidebar').classList.add('open');
  document.getElementById('sidebar-overlay').classList.add('open');
};
document.getElementById('close-sidebar').onclick = closeSidebar;
document.getElementById('sidebar-overlay').onclick = closeSidebar;
function closeSidebar() {
  document.getElementById('sidebar').classList.remove('open');
  document.getElementById('sidebar-overlay').classList.remove('open');
}

async function loadSidebar() {
  try {
    const items = await api('/api/modules/user');
    modules = items;
  } catch { modules = []; }
  modulesCache = {};
  modules.forEach(m => { modulesCache[m.code] = m; });

  const nav = document.getElementById('sidebar-nav');
  const moduleDefs = [
    { code: 'dashboard', name: 'Дашборд', icon: '📊' },
    { code: 'todo', name: 'Задачи', icon: '✅' },
    { code: 'notes', name: 'Заметки', icon: '📝' },
    { code: 'finance', name: 'Финансы', icon: '💰' },
    { code: 'planner', name: 'Планировщик', icon: '📅' }
  ];

  nav.innerHTML = '<div class="sidebar-section-label">Модули</div>' +
    moduleDefs.map(def => {
      const mod = modulesCache[def.code];
      const enabled = mod ? mod.enabled : true;
      const isActive = activeTab === def.code;
      return `<div class="module-item ${isActive ? 'active' : ''}" data-module="${def.code}">
        <div style="display:flex;align-items:center;gap:10px;flex:1" class="module-link">
          <span>${def.icon}</span>
          <span class="module-name">${def.name}</span>
        </div>
        <label class="toggle-switch" onclick="event.stopPropagation()">
          <input type="checkbox" ${enabled ? 'checked' : ''} data-code="${def.code}">
          <span class="toggle-slider"></span>
        </label>
      </div>`;
    }).join('');

  nav.querySelectorAll('.module-link').forEach(el => {
    el.onclick = () => {
      const code = el.closest('.module-item').dataset.module;
      switchTab(code);
      closeSidebar();
    };
  });

  nav.querySelectorAll('.toggle-switch input').forEach(chk => {
    chk.onchange = async () => {
      const code = chk.dataset.code;
      const mod = modulesCache[code];
      if (mod) {
        try {
          await api(`/api/modules/user/${mod.id}`, {
            method: 'PUT',
            body: JSON.stringify({ enabled: chk.checked })
          });
          mod.enabled = chk.checked;
          toast(chk.checked ? 'Модуль включён' : 'Модуль отключён');
          if (activeTab === 'dashboard') loadDashboard();
        } catch { chk.checked = !chk.checked; toast('Ошибка переключения'); }
      }
    };
  });
}

// Tab switching
function switchTab(code) {
  activeTab = code;
  document.querySelectorAll('.tab-content').forEach(t => t.classList.remove('active'));
  const tab = document.getElementById('tab-' + code);
  if (!tab) { switchTab('dashboard'); return; }
  tab.classList.add('active');

  document.querySelectorAll('.module-item').forEach(m => m.classList.remove('active'));
  const mi = document.querySelector(`.module-item[data-module="${code}"]`);
  if (mi) mi.classList.add('active');

  const titles = { dashboard: 'Дашборд', todo: 'Задачи', notes: 'Заметки', finance: 'Финансы', planner: 'Планировщик' };
  document.getElementById('page-title').textContent = titles[code] || code;

  const ha = document.getElementById('header-actions');
  ha.innerHTML = code === 'dashboard' ? '<button class="header-btn" onclick="openConfig()">⚙ Настроить</button>' : '';

  if (code === 'dashboard') loadDashboard();
  else if (code === 'todo') loadTodos();
  else if (code === 'notes') loadNotes();
  else if (code === 'finance') loadFinance();
  else if (code === 'planner') loadEvents();
}

// ====== DASHBOARD ======
async function loadDashboard() {
  const cont = document.getElementById('tab-dashboard');
  try {
    const data = await api('/api/dashboard');
    widgets = data.widgets || [];
  } catch { widgets = []; }

  const cfg = loadDashboardConfig();
  const enabledTypes = cfg.widgets.length ? cfg.widgets : widgets.map(w => w.type);
  const visible = widgets.filter(w => enabledTypes.includes(w.type));

  if (!visible.length) {
    cont.innerHTML = `<div class="dashboard-grid"><div class="dashboard-empty">
      <p>Дашборд пуст</p>
      <button class="btn-primary" onclick="openConfig()">Настроить дашборд</button>
    </div></div>`;
    return;
  }

  cont.innerHTML = '<div class="dashboard-grid">' + visible.map(w => {
    const val = renderWidgetValue(w);
    const stats = renderWidgetStats(w);
    return `<div class="widget">
      <div class="widget-header"><h3>${w.title || ''}</h3></div>
      ${val}
      ${stats}
    </div>`;
  }).join('') + '</div>';
}

function renderWidgetValue(w) {
  if (w.type === 'todo_stats') return `<div class="widget-value">${w.data?.total ?? '—'}</div>`;
  if (w.type === 'finance_balance') return `<div class="widget-value">${(w.data?.balance ?? 0).toLocaleString()} ₽</div>`;
  if (w.type === 'notes_recent') return `<div class="widget-value">${w.data?.count ?? w.data?.total ?? '—'}</div>`;
  if (w.type === 'planner_today') return `<div class="widget-value">${w.data?.count ?? w.data?.total ?? '—'}</div>`;
  return `<div class="widget-value">—</div>`;
}

function renderWidgetStats(w) {
  if (w.type === 'todo_stats') {
    const d = w.data || {};
    return `<div class="widget-stats">
      <span><span class="stat-label">К выполнению</span> ${d.todo ?? 0}</span>
      <span><span class="stat-label">В работе</span> ${d.inProgress ?? 0}</span>
      <span class="stat-green"><span class="stat-label">Готово</span> ${d.done ?? 0}</span>
      <span class="stat-red"><span class="stat-label">Просрочено</span> ${d.overdue ?? 0}</span>
    </div>`;
  }
  if (w.type === 'finance_balance') {
    const d = w.data || {};
    return `<div class="widget-stats">
      <span class="stat-green">+${(d.income ?? 0).toLocaleString()}</span>
      <span class="stat-red">-${(d.expense ?? 0).toLocaleString()}</span>
    </div>`;
  }
  return '';
}

// Dashboard config
function loadDashboardConfig() {
  try { return JSON.parse(localStorage.getItem('dashCfg')) || { widgets: [] }; }
  catch { return { widgets: [] }; }
}

function saveDashboardConfig(cfg) {
  localStorage.setItem('dashCfg', JSON.stringify(cfg));
}

function openConfig() {
  const cfg = loadDashboardConfig();
  const allWidgetTypes = [
    { type: 'todo_stats', label: 'Задачи', desc: 'Статистика по задачам' },
    { type: 'finance_balance', label: 'Финансы', desc: 'Баланс и доходы/расходы' },
    { type: 'notes_recent', label: 'Заметки', desc: 'Количество заметок' },
    { type: 'planner_today', label: 'Планировщик', desc: 'События на сегодня' }
  ];
  const enabled = cfg.widgets.length ? cfg.widgets : allWidgetTypes.map(t => t.type);

  document.getElementById('config-body').innerHTML = allWidgetTypes.map(t =>
    `<div class="config-item">
      <div><div class="config-label">${t.label}</div><div class="config-desc">${t.desc}</div></div>
      <label class="toggle-switch">
        <input type="checkbox" data-ctype="${t.type}" ${enabled.includes(t.type) ? 'checked' : ''}>
        <span class="toggle-slider"></span>
      </label>
    </div>`
  ).join('');

  document.getElementById('config-modal').classList.add('open');
}

document.querySelector('.modal-close').onclick = () => {
  document.getElementById('config-modal').classList.remove('open');
};

document.getElementById('config-save').onclick = () => {
  const checked = document.querySelectorAll('#config-body input[type="checkbox"]:checked');
  const types = Array.from(checked).map(c => c.dataset.ctype);
  saveDashboardConfig({ widgets: types });
  document.getElementById('config-modal').classList.remove('open');
  loadDashboard();
  toast('Дашборд обновлён');
};

// ====== TODOS ======
let todoListeners = [];

document.getElementById('tab-todos').innerHTML = `
  <div class="section-header"><h2>Задачи</h2></div>
  <form id="todo-form" class="inline-form">
    <input type="text" id="todo-title" placeholder="Новая задача..." required>
    <select id="todo-priority"><option value="HIGH">Высокий</option><option value="MEDIUM">Средний</option><option value="LOW">Низкий</option></select>
    <button type="submit">+ Добавить</button>
  </form>
  <ul id="todo-list" class="item-list"></ul>
`;

document.getElementById('todo-form').onsubmit = async (e) => {
  e.preventDefault();
  const title = document.getElementById('todo-title').value.trim();
  if (!title) return;
  try {
    await api('/api/todos', {
      method: 'POST',
      body: JSON.stringify({ title, priority: document.getElementById('todo-priority').value })
    });
    document.getElementById('todo-title').value = '';
    loadTodos();
    toast('Задача создана');
  } catch { toast('Ошибка создания задачи'); }
};

async function loadTodos() {
  try {
    const items = await api('/api/todos');
    const list = document.getElementById('todo-list');
    if (!items || !items.length) {
      list.innerHTML = '<li class="item-empty">Нет задач</li>';
      return;
    }
    list.innerHTML = items.map(t => {
      const statusClass = (t.status || 'TODO').toLowerCase().replace(/\s+/g, '-');
      return `<li>
        <div class="item-left">
          <span class="title">${esc(t.title)}</span>
          <span class="meta">${t.status || 'TODO'}</span>
        </div>
        <div class="item-right">
          <span class="badge badge-${(t.priority || 'medium').toLowerCase()}">${t.priority || 'MEDIUM'}</span>
          ${t.id ? `<button class="btn-delete" onclick="deleteTodo('${t.id}')">✕</button>` : ''}
        </div>
      </li>`;
    }).join('');
  } catch {
    document.getElementById('todo-list').innerHTML = '<li class="item-empty">Ошибка загрузки</li>';
  }
}

async function deleteTodo(id) {
  try { await api(`/api/todos/${id}`, { method: 'DELETE' }); loadTodos(); toast('Задача удалена'); }
  catch { toast('Ошибка удаления'); }
}

// ====== NOTES ======
document.getElementById('tab-notes').innerHTML = `
  <div class="section-header"><h2>Заметки</h2></div>
  <form id="note-form" class="inline-form">
    <input type="text" id="note-title" placeholder="Заголовок..." required>
    <textarea id="note-content" placeholder="Текст заметки..."></textarea>
    <button type="submit">+ Добавить</button>
  </form>
  <ul id="note-list" class="item-list"></ul>
`;

document.getElementById('note-form').onsubmit = async (e) => {
  e.preventDefault();
  const title = document.getElementById('note-title').value.trim();
  if (!title) return;
  try {
    await api('/api/notes', {
      method: 'POST',
      body: JSON.stringify({ title, content: document.getElementById('note-content').value })
    });
    document.getElementById('note-title').value = '';
    document.getElementById('note-content').value = '';
    loadNotes();
    toast('Заметка создана');
  } catch { toast('Ошибка создания заметки'); }
};

async function loadNotes() {
  try {
    const items = await api('/api/notes');
    const list = document.getElementById('note-list');
    if (!items || !items.length) {
      list.innerHTML = '<li class="item-empty">Нет заметок</li>';
      return;
    }
    list.innerHTML = items.map(n =>
      `<li>
        <div class="item-left">
          <span class="title">${esc(n.title)}</span>
          <span class="meta">${esc(n.content || '').substring(0, 100)}</span>
        </div>
        <div class="item-right">
          ${n.id ? `<button class="btn-delete" onclick="deleteNote('${n.id}')">✕</button>` : ''}
        </div>
      </li>`
    ).join('');
  } catch {
    document.getElementById('note-list').innerHTML = '<li class="item-empty">Ошибка загрузки</li>';
  }
}

async function deleteNote(id) {
  try { await api(`/api/notes/${id}`, { method: 'DELETE' }); loadNotes(); toast('Заметка удалена'); }
  catch { toast('Ошибка удаления'); }
}

// ====== FINANCE ======
document.getElementById('tab-finance').innerHTML = `
  <div class="section-header"><h2>Транзакции</h2></div>
  <form id="finance-form" class="inline-form">
    <select id="finance-type"><option value="EXPENSE">Расход</option><option value="INCOME">Доход</option></select>
    <input type="number" id="finance-amount" placeholder="Сумма" step="0.01" required>
    <input type="text" id="finance-category" placeholder="Категория (еда, транспорт...)" required>
    <button type="submit">+ Добавить</button>
  </form>
  <ul id="finance-list" class="item-list"></ul>
  <div class="balance-section" id="finance-balance-section"></div>
`;

document.getElementById('finance-form').onsubmit = async (e) => {
  e.preventDefault();
  const amount = parseFloat(document.getElementById('finance-amount').value);
  if (!amount) return;
  try {
    await api('/api/finance/transactions', {
      method: 'POST',
      body: JSON.stringify({
        type: document.getElementById('finance-type').value,
        amount,
        category: document.getElementById('finance-category').value
      })
    });
    document.getElementById('finance-amount').value = '';
    document.getElementById('finance-category').value = '';
    loadFinance();
    toast('Транзакция добавлена');
  } catch { toast('Ошибка добавления транзакции'); }
};

async function loadFinance() {
  try {
    const items = await api('/api/finance/transactions');
    const list = document.getElementById('finance-list');
    if (!items || !items.length) {
      list.innerHTML = '<li class="item-empty">Нет транзакций</li>';
    } else {
      list.innerHTML = items.map(t =>
        `<li>
          <div class="item-left">
            <span class="title">${esc(t.category || '')}</span>
            <span class="meta">${t.description || ''}</span>
          </div>
          <div class="item-right">
            <span class="badge badge-${(t.type || 'expense').toLowerCase()}">${t.type} ${(t.amount || 0).toFixed(2)} ₽</span>
            ${t.id ? `<button class="btn-delete" onclick="deleteTransaction('${t.id}')">✕</button>` : ''}
          </div>
        </li>`
      ).join('');
    }
  } catch {
    document.getElementById('finance-list').innerHTML = '<li class="item-empty">Ошибка загрузки</li>';
  }

  try {
    const bal = await api('/api/finance/balance');
    document.getElementById('finance-balance-section').innerHTML = `
      <div class="balance-value">${(bal.balance || 0).toFixed(2)} ₽</div>
      <div class="balance-breakdown">
        <span class="balance-income">+${(bal.income || 0).toFixed(2)}</span>
        <span class="balance-expense">-${(bal.expense || 0).toFixed(2)}</span>
      </div>`;
  } catch {}
}

async function deleteTransaction(id) {
  try { await api(`/api/finance/transactions/${id}`, { method: 'DELETE' }); loadFinance(); toast('Транзакция удалена'); }
  catch { toast('Ошибка удаления'); }
}

// ====== PLANNER ======
document.getElementById('tab-planner').innerHTML = `
  <div class="section-header"><h2>События</h2></div>
  <form id="event-form" class="inline-form">
    <input type="text" id="event-title" placeholder="Название события..." required>
    <input type="datetime-local" id="event-date" required>
    <button type="submit">+ Добавить</button>
  </form>
  <ul id="event-list" class="item-list"></ul>
`;

document.getElementById('event-form').onsubmit = async (e) => {
  e.preventDefault();
  const title = document.getElementById('event-title').value.trim();
  if (!title) return;
  const dt = document.getElementById('event-date').value;
  try {
    await api('/api/planner/events', {
      method: 'POST',
      body: JSON.stringify({ title, dateTime: new Date(dt).toISOString(), duration: 60 })
    });
    document.getElementById('event-title').value = '';
    loadEvents();
    toast('Событие создано');
  } catch { toast('Ошибка создания события'); }
};

async function loadEvents() {
  try {
    const items = await api('/api/planner/events');
    const list = document.getElementById('event-list');
    if (!items || !items.length) {
      list.innerHTML = '<li class="item-empty">Нет событий</li>';
      return;
    }
    list.innerHTML = items.map(e =>
      `<li>
        <div class="item-left">
          <span class="title">${esc(e.title)}</span>
          <span class="meta">${e.dateTime ? new Date(e.dateTime).toLocaleString('ru-RU') : ''}</span>
        </div>
        <div class="item-right">
          ${e.id ? `<button class="btn-delete" onclick="deleteEvent('${e.id}')">✕</button>` : ''}
        </div>
      </li>`
    ).join('');
  } catch {
    document.getElementById('event-list').innerHTML = '<li class="item-empty">Ошибка загрузки</li>';
  }
}

async function deleteEvent(id) {
  try { await api(`/api/planner/events/${id}`, { method: 'DELETE' }); loadEvents(); toast('Событие удалено'); }
  catch { toast('Ошибка удаления'); }
}

// Utility
function esc(s) { if (!s) return ''; const d = document.createElement('div'); d.textContent = String(s); return d.innerHTML; }

// Init
if (token) { enterApp(); } else { document.getElementById('auth-page').classList.add('active'); }
