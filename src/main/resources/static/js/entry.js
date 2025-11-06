const doneTaskInput = document.getElementById('doneTaskInput');
const addDoneTaskBtn = document.getElementById('addDoneTaskBtn');
const doneTaskList = document.getElementById('doneTaskList');
const doneTaskHidden = document.getElementById('doneTaskHidden');
let doneTasks = [];

addDoneTaskBtn.addEventListener('click', function() {
    const value = doneTaskInput.value.trim();
    if (value) {
        doneTasks.push(value);
        const li = document.createElement('li');
        li.textContent = value;
        doneTaskList.appendChild(li);
        doneTaskInput.value = '';
        doneTaskHidden.value = doneTasks.join('\n');
    }
});

doneTaskInput.addEventListener('keydown', function(event) {
    if (event.key === 'Enter') {
        event.preventDefault();
        addDoneTaskBtn.click();
    }
});

// 明日やること（toneTask）
const tomorrowTaskInput = document.getElementById('tomorrowTaskInput');
const addTomorrowTaskBtn = document.getElementById('addTomorrowTaskBtn');
const tomorrowTaskList = document.getElementById('tomorrowTaskList');
const tomorrowTaskHidden = document.getElementById('tomorrowTaskHidden');
let tomorrowTasks = [];

addTomorrowTaskBtn.addEventListener('click', function() {
    const value = tomorrowTaskInput.value.trim();
    if (value) {
        tomorrowTasks.push(value);
        const li = document.createElement('li');
        li.textContent = value;
        tomorrowTaskList.appendChild(li);
        tomorrowTaskInput.value = '';
        tomorrowTaskHidden.value = tomorrowTasks.join('\n');
    }
});

tomorrowTaskInput.addEventListener('keydown', function(event) {
    if (event.key === 'Enter') {
        event.preventDefault();
        addTomorrowTaskBtn.click();
    }
});