import request from '@/utils/request'

export function fetchSchedules() {
  return request({
    url: '/system/schedules',
    method: 'get'
  })
}

export function submitSchedule(schedule) {
  return request({
    url: '/system/schedule',
    method: 'post',
    data: {
      data: schedule
    }
  })
}

export function executeSchedule(runner) {
  return request({
    url: `/system/schedule/${runner}`,
    method: 'post'
  })
}

export function resetSchedule(runner) {
  return request({
    url: `/system/schedule/${runner}`,
    method: 'put'
  })
}

export function fetchScheduleHistories(runner, page) {
  return request({
    url: `/system/schedule/histories`,
    method: 'post',
    data: {
      data: {
        searchParam: runner,
        page
      }
    }
  })
}
