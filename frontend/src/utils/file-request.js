import axios from 'axios'
// import Vue from 'vue'
import { Message } from 'element-ui'

export function upload({ url, data }, name, size) {
  const instance = axios.create({
    baseURL: process.env.VUE_APP_BASE_API,
    withCredentials: true
  })
  // if (!checkFileSize(data, name, size)) {
  //   Message({
  //     message: '文件超大',
  //     type: 'error',
  //     duration: 5 * 1000
  //   })
  //   return
  // }
  return instance.post(url, data, { transformRequest: [function(data, headers) {
    if (headers['Content-Type'] === 'multipart/form-data') {
      return data
    } else {
      headers['Content-Type'] = 'application/json'
    }
    return JSON.stringify(data)
  }], headers: { 'Content-Type': 'multipart/form-data' }}).then(response => {
    if (!response) {
      Message({
        message: '上传失败，请联系管理员',
        type: 'error',
        duration: 5 * 1000
      })
    } else {
      const res = response.data
      // console.log('response: ', res)
      if (res.flag !== 0) {
        Message({
          message: '上传失败，请联系管理员',
          type: 'error',
          duration: 5 * 1000
        })
      } else {
        Message({
          message: '上传成功',
          type: 'success',
          duration: 5 * 1000
        })
        return res.data
      }
    }
  }).catch(message => {
    Message({
      message: '上传失败，请联系管理员',
      type: 'error',
      duration: 5 * 1000
    })
    // console.log('message:', message.toLocaleString())
    // const msg = message.toLocaleString()
    // Message({
    //   message: msg,
    //   type: 'error',
    //   duration: 5 * 1000
    // })
  })
}

// function checkFileSize(data, name, size) {
//   // const _name = name || 'data.attach'
//   const _file = data.get(name) || data.get('attach') || data.get('data.attach')
//   const _size = size || 5
//   const MAX_SIZE = _size * 1024 * 1024
//   if (_file) {
//     return _file.size <= MAX_SIZE
//   } else {
//     Message({
//       message: '文件参数不正确',
//       type: 'warning',
//       duration: 5 * 1000
//     })
//     return true
//   }
// }

function downFile(blob, fileName) {
  // 非IE下载
  if ('download' in document.createElement('a')) {
    const link = document.createElement('a')
    link.href = window.URL.createObjectURL(blob) // 创建下载的链接
    link.download = fileName // 下载后文件名
    link.style.display = 'none'
    document.body.appendChild(link)
    link.click() // 点击下载
    window.URL.revokeObjectURL(link.href) // 释放掉blob对象
    document.body.removeChild(link) // 下载完成移除元素
  } else {
    // IE10+下载
    window.navigator.msSaveBlob(blob, fileName)
  }
}

// 下载文件
export function downloadFile(url) {
  // 响应类型：arraybuffer, blob
  const instance = axios.create({
    baseURL: process.env.VUE_APP_BASE_API,
    withCredentials: true
  })
  instance.get(url, { responseType: 'blob' }).then(response => {
    const headers = response.headers
    const contentType = headers['content-type']

    if (!response.data) {
      return false
    } else if (response.data.type === 'application/json') {
      const code = response.headers['errcode']
      if (code === '1001') {
        Message.error('目前没有人完成填表！')
      } else {
        Message.error('下载结果异常，请联系系统管理员！')
      }
    } else {
      const blob = new Blob([response.data], { type: contentType })
      const contentDisposition = response.headers['content-disposition']
      let fileName = 'unknown'
      if (contentDisposition) {
        fileName = window.decodeURI(response.headers['content-disposition'].split('=')[1]).replace(/\"/gi, '')
      }
      downFile(blob, fileName)
    }
  })
}
