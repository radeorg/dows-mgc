<!DOCTYPE html>
<html lang="en">
<head>
    <title>聊天</title>
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" type="text/css" href="/api/css/chat.css"/>
</head>
<body>
<div class="container">
    <div class="content">
        <div class="item item-center"><span>快速构建单表维护</span></div>
        <div class="item item-left">
            <div class="avatar"><img src="/api/assets/ai.png"/></div>
            <div class="bubble bubble-left">请告诉我您的需求吧，我会根据您的需求帮您构建单表维护界面</div>
        </div>
    </div>
    <div class="input-area">
        <textarea name="text" id="textarea"></textarea>
        <div class="button-area">
            <button id="send-btn" onclick="send()">发 送</button>
        </div>
    </div>
</div>
<script>
    const content = document.querySelector('.content')

    document.onkeyup = function (e) {
        if (window.event)//如果window.event对象存在，就以此事件对象为准
            e = window.event;
        let code = e.charCode || e.keyCode;
        if (code === 13) {
            //此处编写用户敲回车后的代码
            send()
        }
    }

    function send() {
        let textarea = document.getElementById('textarea');
        let sendBtn = document.getElementById("send-btn")
        let text = textarea.value;
        if (!text) {
            alert('请输入内容');
            return;
        }

        // user
        let userItem = document.createElement('div');
        userItem.className = 'item item-right';
        userItem.innerHTML = `<div class="bubble bubble-left">` + text + `</div><div class="avatar"><img src="/api/assets/user.png"/></div>`;
        content.appendChild(userItem);

        //滚动条置底
        content.scrollTop = content.scrollHeight;

        //清空并禁止输入
        textarea.value = '';
        textarea.disabled = true;
        sendBtn.disabled = true;

        sendBtn.textContent = '正在发送...';

        const id = Date.now();
        const id_think = id + "-think"
        const id_text = id + "-text"
        let think = false;

        let assistantItem = document.createElement('div');
        assistantItem.className = 'item item-left';
        assistantItem.innerHTML = `<div class="avatar">
                                        <img src="/api/assets/ai.png"/>
                                   </div>
                                   <div class="bubble bubble-left" id="` + id + `">
                                        <span id="` + id_think + `" style="color: #999999"></span>
                                        <span id="` + id_text + `"></span>
                                   </div>`;
        content.appendChild(assistantItem);
        let bubbleItem = document.getElementById(id);
        let thinkItem = document.getElementById(id_think);
        let textItem = document.getElementById(id_text);

        const eventSource = new EventSource('/api/mgc/buildRequirement?text=' + text);

        eventSource.onmessage = (event) => {
            let message = event.data.substring(1, event.data.length - 1);
            console.log('收到数据:', message)

            if (message == "<COMPLETE>") {
                eventSource.close();
                textarea.disabled = false;
                sendBtn.disabled = false;
                sendBtn.textContent = '发 送';

                const button = document.createElement('button');
                button.textContent = "生成标准类代码";
                bubbleItem.appendChild(button);
                button.addEventListener('click', function () {
                    sendClass(textItem.textContent)
                })

                return;
            }

            if (message === '<think>') {
                think = true;
                thinkItem.textContent = thinkItem.textContent + "思考";
                return;
            } else if (message === '</think>') {
                think = false;
                return;
            }

            if (think) {
                thinkItem.textContent = thinkItem.textContent + message;
            } else {
                textItem.textContent = textItem.textContent + message;
            }

            //滚动条置底
            content.scrollTop = content.scrollHeight;
        };

        eventSource.onerror = (err) => {
            console.error('EventSource 错误:', err);
            eventSource.close();
            textarea.disabled = false;
            sendBtn.disabled = false;
            sendBtn.textContent = '发 送';
        };
    }


    function sendClass(text) {
        let textarea = document.getElementById('textarea');
        let sendBtn = document.getElementById("send-btn")
        //清空并禁止输入
        textarea.value = '';
        textarea.disabled = true;
        sendBtn.disabled = true;

        sendBtn.textContent = '正在发送...';

        const id = Date.now();
        const id_think = id + "-think"
        const id_text = id + "-text"
        let think = false;

        let assistantItem = document.createElement('div');
        assistantItem.className = 'item item-left';
        assistantItem.innerHTML = `<div class="avatar">
                                        <img src="/api/assets/ai.png"/>
                                   </div>
                                   <div class="bubble bubble-left" id="` + id + `">
                                        <span id="` + id_think + `" style="color: #999999"></span>
                                        <span id="` + id_text + `"></span>
                                   </div>`;
        content.appendChild(assistantItem);
        let bubbleItem = document.getElementById(id);
        let thinkItem = document.getElementById(id_think);
        let textItem = document.getElementById(id_text);

        const eventSource = new EventSource('/api/mgc/buildClass?text=' + text);

        eventSource.onmessage = (event) => {
            let message = event.data.substring(1, event.data.length - 1);
            console.log('收到数据:', message)

            if (message == "<COMPLETE>") {
                eventSource.close();
                textarea.disabled = false;
                sendBtn.disabled = false;
                sendBtn.textContent = '发 送';

                const button = document.createElement('button');
                button.textContent = "加载功能";
                bubbleItem.appendChild(button);
                button.addEventListener('click', function () {
                    console.log("加载功能")

                    fetch('/api/mgc/buildWeb', {
                        method: 'POST',
                        headers: {'Content-Type': 'application/json'},
                        body: JSON.stringify({text: textItem.textContent}),
                    })
                        .then(response => {
                            if (!response.ok) {
                                throw new Error('Network response was not ok');
                            }
                            return response.json();
                        })
                        .then(json => {
                            let assistantItem = document.createElement('div');
                            assistantItem.className = 'item item-left';
                            assistantItem.innerHTML = `<div class="avatar"><img src="/api/assets/ai.png"/></div><div class="bubble bubble-left"><a href="/api/entity/view/` + json.id + `" target="_blank">` + `打开页面` + `</a></div>`;
                            document.querySelector('.content').appendChild(assistantItem);

                            textarea.disabled = false;
                            sendBtn.disabled = false;
                            sendBtn.textContent = '发 送';
                        })
                        .catch(error => {
                            textarea.disabled = false;
                            sendBtn.disabled = false;
                            sendBtn.textContent = '发 送';
                            console.error('There has been a problem with your fetch operation:', error);
                        });
                })
                return;
            }

            if (message === '<think>') {
                think = true;
                thinkItem.textContent = thinkItem.textContent + "思考";
                return;
            } else if (message === '</think>') {
                think = false;
                return;
            }

            if (think) {
                thinkItem.textContent = thinkItem.textContent + message;
            } else {
                textItem.textContent = textItem.textContent + message;
            }

            //滚动条置底
            content.scrollTop = content.scrollHeight;
        };

        eventSource.onerror = (err) => {
            console.error('EventSource 错误:', err);
            eventSource.close();
            textarea.disabled = false;
            sendBtn.disabled = false;
            sendBtn.textContent = '发 送';
        };
    }
</script>
</body>
</html>