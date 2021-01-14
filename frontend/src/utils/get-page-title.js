import settings from '@/settings'

export default function getPageTitle(pageTitle) {
  const title = window._title || settings.title || 'Vue Element Admin'
  if (pageTitle) {
    return `${pageTitle} - ${title}`
  }
  return `${title}`
}
