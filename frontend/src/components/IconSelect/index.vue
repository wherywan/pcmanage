<template>
  <div class="ui-fas">
    <el-input
      ref="input"
      v-model="name"
      v-popover:popover
      :placeholder="placeholder"
      clearable
      :disabled="disabled"
      :readonly="readonly"
      @focus="_popoverShowFun"
    >
      <template slot="prepend">
        <svg-icon :icon-class="prefixIcon" class-name="custom-class" />
      </template>
    </el-input>
    <!-- 弹出框 -->
    <el-popover
      ref="popover"
      v-model="visible"
      :disabled="disabled"
      :placement="placement"
      popper-class="el-icon-popper"
      :width="width"
      trigger="click"
    >
      <el-scrollbar tag="div" wrap-class="el-select-dropdown__wrap" view-class="el-select-dropdown__list" class="is-empty">
        <el-input v-model="filterText" class="fas-icon-input" clearable :placeholder="placeholder" />
        <ul class="fas-icon-list">
          <li v-for="(item, index) in iconList" :key="index" @click="selectedIcon(item)">
            <svg-icon :icon-class="item" class-name="custom-class" />
            <span>{{ item }}</span>
          </li>
        </ul>
      </el-scrollbar>
    </el-popover>
  </div>
</template>

<script>
import { on, off } from '@/utils/dom'
import svgIcons from '@/utils/svg-icons'
export default {
  name: 'IconSelect',
  props: {
    value: {
      type: String,
      default: ''
    },
    placement: {
      type: String,
      default: 'bottom'
    },
    placeholder: {
      type: String,
      default: () => { return '请输入图标名称' }
    },
    disabled: {
      type: Boolean,
      default: () => { return false }
    }
  },
  data() {
    return {
      iconList: svgIcons,
      filterText: '',
      readonly: true,
      visible: false,
      width: 200,
      prefixIcon: 'icon',
      name: ''
    }
  },
  watch: {
    name: function(val) {
      this.readonly = !val
      setTimeout(() => {
        this.prefixIcon = val || 'icon'
      }, 200)
    },
    value: function(val) {
      setTimeout(() => {
        this.name = val
      }, 200)
    },
    filterText: function(val) {
      if (val) {
        const list = this.iconList
        this.iconList = list.filter((item) => item.indexOf(val) !== -1)
      } else {
        this.iconList = svgIcons
      }
    }
  },
  created() {
    this.prefixIcon = this.value ? this.value : 'icon'
    this.name = this.value
  },
  mounted() {
    this._updateW()
    this.$nextTick(() => {
      on(document, 'mouseup', this._popoverHideFun)
    })
  },
  beforeDestroy() {
    off(document, 'mouseup', this._popoverHideFun)
  },
  methods: {
    selectedIcon(item) {
      this.visible = false
      this.name = item
      this._emitFun()
    },
    // 更新宽度
    _updateW() {
      this.$nextTick(() => {
        this.width = this.$refs.input.$el.getBoundingClientRect().width
      })
    },
    // 显示弹出框的时候容错，查看是否和el宽度一致
    _popoverShowFun() {
      this._updateW()
    },
    // 点击控件外，判断是否隐藏弹出框
    _popoverHideFun(e) {
      const isInter = e.path.some(list => {
        const name = list.className
        return name && typeof (name) === 'string' && (name.indexOf('fas-icon-list') !== -1 || name.indexOf('fas-icon-input') !== -1)
      })
      if (!isInter) {
        this.visible = false
      }
    },
    // 判断类型，抛出当前选中id
    _emitFun() {
      this.$emit('input', this.name)
      this._updatePopoverLocationFun()
    },
    // 更新popover位置
    _updatePopoverLocationFun() {
      // dom高度还没有更新，做一个延迟
      setTimeout(() => {
        this.$refs.popover.updatePopper()
      }, 50)
    }
  }
}
</script>

<style scoped>
.fas-icon-input {
  margin-bottom: 5px;
}
.fas-icon-list {
  list-style-type: none;
  margin: 0;
  padding: 0;
  display: flex;
  flex-direction: row;
  flex-wrap: wrap;
}
.fas-icon-list li {
  width: 120px;
  height: 15px;
  margin: 5px 1px;
}
</style>
