import { getToken } from '@/utils/auth'

const ws_handlers = {}
let ws

export const getSocket = function() {
  if (ws && ws.readyState !== 3) {
    return ws
  } else {
    const token = getToken()
    ws = new WebSocket(`ws://${window.location.host}${process.env.VUE_APP_BASE_API}/common/ws/${token}`)
    ws.onmessage = function(evt) {
      const msg_str = evt.data
      try {
        const data = JSON.parse(msg_str)
        const channelId = data.channelId
        const handler = ws_handlers[channelId]
        if (handler) {
          handler(data)
        }
      } catch (e) {
        console.log(e)
      }
    }
    ws.onerror = function(code, reason) {
      console.log('异常关闭')
    }
    return ws
  }
}

export const addHandler = function(channelId, handler) {
  ws_handlers[channelId] = handler
}

export const delHandler = function(channelId) {
  delete ws_handlers[channelId]
}

export const sendMessage = function(channelId, data) {
  const ws = getSocket()
  console.log(ws)
  const msg = JSON.stringify({ channelId, data })
  if (!ws || ws.readyState === 0) {
    setTimeout(function() {
      ws.send(msg)
    }, 200)
  } else {
    ws.send(msg)
  }
}
