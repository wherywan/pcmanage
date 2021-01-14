const req = require.context('../../public/icons', false, /\.svg$/)
const requireAll = requireContext => requireContext.keys()

const re = /\.\/(.*)\.svg/

const PngIcons = requireAll(req).map(i => {
  return i.match(re)[1]
})

export default PngIcons
