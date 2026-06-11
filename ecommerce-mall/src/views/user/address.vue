<template>
  <div class="address-page">
    <AppHeader />
    <main class="main">
      <div class="container">
        <div class="center-layout">
          <UserSidebar active="address" />

          <!-- 内容区 -->
          <div class="content">
            <div class="page-header">
              <div class="header-left">
                <h2>收货地址</h2>
                <span class="address-count">共 {{ addresses.length }} 个地址</span>
              </div>
              <button class="btn-add" @click="handleAdd">
                <el-icon><Plus /></el-icon>
                新增地址
              </button>
            </div>

            <!-- 地址列表 -->
            <div v-if="addresses.length" class="address-grid">
              <div
                v-for="addr in addresses"
                :key="addr.id"
                :class="['address-card', { 'is-default': addr.isDefault === 1 }]"
              >
                <div class="card-header">
                  <div class="user-info">
                    <div class="user-avatar">{{ addr.receiverName.charAt(0) }}</div>
                    <div class="user-detail">
                      <span class="name">{{ addr.receiverName }}</span>
                      <span class="phone">{{ addr.phone }}</span>
                    </div>
                  </div>
                  <el-tag v-if="addr.isDefault === 1" type="primary" effect="dark" size="small" round>
                    默认
                  </el-tag>
                </div>
                <div class="card-body">
                  <div class="address-text">
                    <el-icon><Location /></el-icon>
                    <span>{{ addr.province }}{{ addr.city }}{{ addr.district }}{{ addr.detail }}</span>
                  </div>
                </div>
                <div class="card-footer">
                  <button class="btn-action" @click="handleEdit(addr)">
                    <el-icon><Edit /></el-icon>
                    编辑
                  </button>
                  <button
                    v-if="addr.isDefault !== 1"
                    class="btn-action"
                    @click="handleSetDefault(addr.id)"
                  >
                    <el-icon><CircleCheck /></el-icon>
                    设为默认
                  </button>
                  <button class="btn-action btn-delete" @click="handleDelete(addr.id)">
                    <el-icon><Delete /></el-icon>
                    删除
                  </button>
                </div>
              </div>
            </div>

            <div v-else class="empty-state">
              <div class="empty-icon">
                <el-icon :size="48"><Location /></el-icon>
              </div>
              <h3>暂无收货地址</h3>
              <p>添加一个收货地址，方便您快速下单</p>
              <button class="btn-add-empty" @click="handleAdd">
                <el-icon><Plus /></el-icon>
                新增地址
              </button>
            </div>
          </div>
        </div>
      </div>
    </main>
    <AppFooter />

    <!-- 新增/编辑对话框 -->
    <Transition name="fade">
      <div v-if="showDialog" class="dialog-overlay" @click.self="showDialog = false">
        <Transition name="slide-up">
          <div v-if="showDialog" class="dialog">
            <!-- 对话框头部 -->
            <div class="dialog-header">
              <div class="dialog-title">
                <div class="title-icon">
                  <el-icon :size="20"><Location /></el-icon>
                </div>
                <h3>{{ dialogTitle }}</h3>
              </div>
              <button class="btn-close" @click="showDialog = false">
                <el-icon><Close /></el-icon>
              </button>
            </div>

            <!-- 对话框内容 -->
            <form @submit.prevent="handleSubmit" class="dialog-body">
              <!-- 联系人信息 -->
              <div class="form-section">
                <div class="section-title">
                  <el-icon><User /></el-icon>
                  <span>联系人信息</span>
                </div>
                <div class="form-row">
                  <div class="form-group">
                    <label>
                      收货人
                      <span class="required">*</span>
                    </label>
                    <div class="input-wrapper">
                      <el-icon class="input-icon"><User /></el-icon>
                      <input v-model="form.receiverName" placeholder="请输入收货人姓名" required />
                    </div>
                  </div>
                  <div class="form-group">
                    <label>
                      联系电话
                      <span class="required">*</span>
                    </label>
                    <div class="input-wrapper">
                      <el-icon class="input-icon"><Phone /></el-icon>
                      <input v-model="form.phone" placeholder="请输入联系电话" required />
                    </div>
                  </div>
                </div>
              </div>

              <!-- 所在地区 -->
              <div class="form-section">
                <div class="section-title">
                  <el-icon><Location /></el-icon>
                  <span>所在地区</span>
                </div>
                <div class="form-row form-row-3">
                  <div class="form-group">
                    <label>省份</label>
                    <div class="input-wrapper">
                      <input v-model="form.province" placeholder="请输入省份" />
                    </div>
                  </div>
                  <div class="form-group">
                    <label>城市</label>
                    <div class="input-wrapper">
                      <input v-model="form.city" placeholder="请输入城市" />
                    </div>
                  </div>
                  <div class="form-group">
                    <label>区县</label>
                    <div class="input-wrapper">
                      <input v-model="form.district" placeholder="请输入区县" />
                    </div>
                  </div>
                </div>
              </div>

              <!-- 详细地址 -->
              <div class="form-section">
                <div class="section-title">
                  <el-icon><House /></el-icon>
                  <span>详细地址</span>
                </div>
                <div class="form-group">
                  <label>
                    详细地址
                    <span class="required">*</span>
                  </label>
                  <div class="input-wrapper textarea-wrapper">
                    <textarea
                      v-model="form.detail"
                      placeholder="请输入详细地址（街道、小区、门牌号等）"
                      required
                      rows="3"
                    ></textarea>
                  </div>
                  <div class="form-tip">详细的地址信息有助于快递员准确配送</div>
                </div>
              </div>

              <!-- 默认地址 -->
              <div class="form-section">
                <div class="default-switch">
                  <div class="switch-info">
                    <el-icon><Star /></el-icon>
                    <div>
                      <span class="switch-label">设为默认收货地址</span>
                      <span class="switch-desc">下单时自动选择该地址</span>
                    </div>
                  </div>
                  <label class="toggle">
                    <input type="checkbox" v-model="isDefault" />
                    <span class="toggle-slider"></span>
                  </label>
                </div>
              </div>

              <!-- 底部按钮 -->
              <div class="dialog-footer">
                <button type="button" class="btn-cancel" @click="showDialog = false">
                  取消
                </button>
                <button type="submit" class="btn-submit">
                  <el-icon><Check /></el-icon>
                  保存地址
                </button>
              </div>
            </form>
          </div>
        </Transition>
      </div>
    </Transition>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import AppHeader from '@/components/AppHeader.vue'
import AppFooter from '@/components/AppFooter.vue'
import UserSidebar from '@/components/UserSidebar.vue'
import { useUserStore } from '@/store/user'
import { getAddressList, addAddress, updateAddress, deleteAddress, setDefaultAddress } from '@/api/address'

const userStore = useUserStore()

const addresses = ref([])
const showDialog = ref(false)
const dialogTitle = ref('新增地址')
const isDefault = ref(false)

const form = reactive({
  id: null,
  receiverName: '',
  phone: '',
  province: '',
  city: '',
  district: '',
  detail: ''
})

const loadAddresses = async () => {
  try {
    const res = await getAddressList(userStore.userInfo?.id)
    if (res.code === 200) {
      addresses.value = res.data || []
    }
  } catch (error) {
    console.error(error)
  }
}

const handleAdd = () => {
  dialogTitle.value = '新增地址'
  Object.assign(form, { id: null, receiverName: '', phone: '', province: '', city: '', district: '', detail: '' })
  isDefault.value = false
  showDialog.value = true
}

const handleEdit = (addr) => {
  dialogTitle.value = '编辑地址'
  Object.assign(form, addr)
  isDefault.value = addr.isDefault === 1
  showDialog.value = true
}

const handleSubmit = async () => {
  if (!form.receiverName || !form.phone || !form.detail) {
    ElMessage.warning('请填写完整信息')
    return
  }

  const data = { ...form, userId: userStore.userInfo?.id, isDefault: isDefault.value ? 1 : 0 }

  try {
    if (form.id) {
      await updateAddress(data)
      ElMessage.success('修改成功')
    } else {
      await addAddress(data)
      ElMessage.success('新增成功')
    }
    showDialog.value = false
    loadAddresses()
  } catch (error) {
    console.error(error)
  }
}

const handleDelete = async (id) => {
  await ElMessageBox.confirm('确定要删除该地址吗？', '提示', { type: 'warning' })
  try {
    await deleteAddress(id)
    ElMessage.success('已删除')
    loadAddresses()
  } catch (error) {
    console.error(error)
  }
}

const handleSetDefault = async (id) => {
  try {
    await setDefaultAddress(id, userStore.userInfo?.id)
    ElMessage.success('已设为默认')
    loadAddresses()
  } catch (error) {
    console.error(error)
  }
}

onMounted(() => {
  loadAddresses()
})
</script>

<style scoped>
.main {
  min-height: calc(100vh - 64px - 300px);
  padding: 24px 0;
}

.center-layout {
  display: flex;
  gap: 24px;
}

/* 内容区 */
.content {
  flex: 1;
  min-width: 0;
}

/* 页面头部 */
.page-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 24px;
}

.header-left {
  display: flex;
  align-items: baseline;
  gap: 12px;
}

.header-left h2 {
  font-size: 20px;
  font-weight: 600;
  color: #333;
}

.address-count {
  font-size: 13px;
  color: #999;
}

.btn-add {
  display: flex;
  align-items: center;
  gap: 6px;
  padding: 10px 20px;
  background: #333;
  color: #fff;
  border: none;
  border-radius: 10px;
  font-size: 14px;
  cursor: pointer;
  transition: all 0.3s;
}

.btn-add:hover {
  background: #444;
  transform: translateY(-2px);
}

/* 地址网格 */
.address-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 16px;
}

.address-card {
  background: #fff;
  border-radius: 16px;
  padding: 20px;
  border: 2px solid #f0f0f0;
  transition: all 0.3s;
}

.address-card:hover {
  border-color: #e0e0e0;
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.06);
  transform: translateY(-2px);
}

.address-card.is-default {
  border-color: #333;
  background: #fff;
  position: relative;
}

.address-card.is-default::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  height: 3px;
  background: #333;
  border-radius: 16px 16px 0 0;
}

.card-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 16px;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 12px;
}

.user-avatar {
  width: 40px;
  height: 40px;
  border-radius: 12px;
  background: #333;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #fff;
  font-weight: 600;
  font-size: 16px;
}

.user-detail {
  display: flex;
  flex-direction: column;
  gap: 2px;
}

.name {
  font-size: 15px;
  font-weight: 600;
  color: #333;
}

.phone {
  font-size: 13px;
  color: #999;
}

.card-body {
  margin-bottom: 16px;
}

.address-text {
  display: flex;
  align-items: flex-start;
  gap: 8px;
  font-size: 14px;
  color: #666;
  line-height: 1.6;
}

.address-text .el-icon {
  margin-top: 2px;
  color: #999;
  flex-shrink: 0;
}

.card-footer {
  display: flex;
  gap: 8px;
  padding-top: 16px;
  border-top: 1px solid #f5f5f5;
}

.btn-action {
  display: flex;
  align-items: center;
  gap: 4px;
  padding: 6px 12px;
  background: #f5f5f5;
  border: none;
  border-radius: 8px;
  font-size: 13px;
  color: #666;
  cursor: pointer;
  transition: all 0.2s;
}

.btn-action:hover {
  background: #eee;
  color: #333;
}

.btn-action.btn-delete:hover {
  background: #fff0f0;
  color: #ff4757;
}

/* 空状态 */
.empty-state {
  text-align: center;
  padding: 60px 20px;
  background: #fff;
  border-radius: 16px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.04);
}

.empty-icon {
  width: 80px;
  height: 80px;
  background: linear-gradient(135deg, #f0f0f0 0%, #e8e8e8 100%);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  margin: 0 auto 20px;
  color: #ccc;
}

.empty-state h3 {
  font-size: 16px;
  color: #333;
  margin-bottom: 8px;
}

.empty-state p {
  font-size: 14px;
  color: #999;
  margin-bottom: 24px;
}

.btn-add-empty {
  display: inline-flex;
  align-items: center;
  gap: 6px;
  padding: 12px 24px;
  background: #333;
  color: #fff;
  border: none;
  border-radius: 10px;
  font-size: 14px;
  cursor: pointer;
  transition: all 0.3s;
}

.btn-add-empty:hover {
  background: #444;
  transform: translateY(-2px);
}

/* 对话框动画 */
.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.3s ease;
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}

.slide-up-enter-active,
.slide-up-leave-active {
  transition: all 0.3s ease;
}

.slide-up-enter-from {
  opacity: 0;
  transform: translateY(30px) scale(0.95);
}

.slide-up-leave-to {
  opacity: 0;
  transform: translateY(30px) scale(0.95);
}

/* 对话框 */
.dialog-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
  backdrop-filter: blur(8px);
}

.dialog {
  background: #fff;
  border-radius: 20px;
  width: 560px;
  max-height: 90vh;
  overflow-y: auto;
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.15);
}

/* 对话框头部 */
.dialog-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 24px 28px;
  border-bottom: 1px solid #f0f0f0;
}

.dialog-title {
  display: flex;
  align-items: center;
  gap: 12px;
}

.title-icon {
  width: 40px;
  height: 40px;
  background: #333;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #fff;
}

.dialog-header h3 {
  font-size: 18px;
  font-weight: 600;
  color: #333;
}

.btn-close {
  width: 36px;
  height: 36px;
  background: #f5f5f5;
  border: none;
  border-radius: 10px;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #999;
  transition: all 0.2s;
}

.btn-close:hover {
  background: #eee;
  color: #333;
}

/* 对话框内容 */
.dialog-body {
  padding: 24px 28px;
}

.form-section {
  margin-bottom: 24px;
}

.section-title {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 14px;
  font-weight: 600;
  color: #333;
  margin-bottom: 16px;
  padding-bottom: 12px;
  border-bottom: 1px solid #f5f5f5;
}

.section-title .el-icon {
  color: #333;
}

.form-row {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 16px;
}

.form-row-3 {
  grid-template-columns: repeat(3, 1fr);
}

.form-group {
  margin-bottom: 16px;
}

.form-group label {
  display: flex;
  align-items: center;
  gap: 4px;
  font-size: 13px;
  font-weight: 500;
  color: #666;
  margin-bottom: 8px;
}

.required {
  color: #ff4757;
  font-size: 14px;
}

.input-wrapper {
  position: relative;
  display: flex;
  align-items: center;
}

.input-icon {
  position: absolute;
  left: 14px;
  color: #ccc;
  font-size: 16px;
  z-index: 1;
}

.input-wrapper input {
  width: 100%;
  height: 44px;
  padding: 0 16px;
  padding-left: 40px;
  border: 2px solid #f0f0f0;
  border-radius: 12px;
  font-size: 14px;
  outline: none;
  transition: all 0.2s;
  box-sizing: border-box;
  background: #fafafa;
}

.input-wrapper input:focus {
  border-color: #333;
  background: #fff;
}

.input-wrapper input::placeholder {
  color: #ccc;
}

.form-row .input-wrapper input {
  padding-left: 16px;
}

.textarea-wrapper {
  align-items: flex-start;
}

.textarea-wrapper textarea {
  width: 100%;
  padding: 12px 16px;
  border: 2px solid #f0f0f0;
  border-radius: 12px;
  font-size: 14px;
  outline: none;
  transition: all 0.2s;
  box-sizing: border-box;
  background: #fafafa;
  resize: vertical;
  font-family: inherit;
  line-height: 1.5;
}

.textarea-wrapper textarea:focus {
  border-color: #333;
  background: #fff;
}

.textarea-wrapper textarea::placeholder {
  color: #ccc;
}

.form-tip {
  font-size: 12px;
  color: #999;
  margin-top: 8px;
  display: flex;
  align-items: center;
  gap: 4px;
}

/* 默认地址开关 */
.default-switch {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 16px 20px;
  background: #f8f9fa;
  border-radius: 12px;
}

.switch-info {
  display: flex;
  align-items: center;
  gap: 12px;
}

.switch-info .el-icon {
  color: #333;
  font-size: 20px;
}

.switch-label {
  display: block;
  font-size: 14px;
  font-weight: 500;
  color: #333;
}

.switch-desc {
  display: block;
  font-size: 12px;
  color: #999;
  margin-top: 2px;
}

/* Toggle开关 */
.toggle {
  position: relative;
  display: inline-block;
  width: 48px;
  height: 26px;
  cursor: pointer;
}

.toggle input {
  opacity: 0;
  width: 0;
  height: 0;
}

.toggle-slider {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: #ddd;
  border-radius: 26px;
  transition: all 0.3s;
}

.toggle-slider::before {
  content: '';
  position: absolute;
  width: 20px;
  height: 20px;
  left: 3px;
  bottom: 3px;
  background: #fff;
  border-radius: 50%;
  transition: all 0.3s;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.toggle input:checked + .toggle-slider {
  background: #333;
}

.toggle input:checked + .toggle-slider::before {
  transform: translateX(22px);
}

/* 底部按钮 */
.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
  margin-top: 24px;
  padding-top: 20px;
  border-top: 1px solid #f0f0f0;
}

.btn-cancel {
  padding: 12px 24px;
  background: #f5f5f5;
  border: none;
  border-radius: 12px;
  font-size: 14px;
  color: #666;
  cursor: pointer;
  transition: all 0.2s;
}

.btn-cancel:hover {
  background: #eee;
}

.btn-submit {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 12px 28px;
  background: #333;
  border: none;
  border-radius: 12px;
  font-size: 14px;
  color: #fff;
  cursor: pointer;
  transition: all 0.3s;
}

.btn-submit:hover {
  background: #444;
  transform: translateY(-2px);
}
</style>
