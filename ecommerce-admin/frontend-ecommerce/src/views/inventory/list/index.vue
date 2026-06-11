<template>
  <div class="inventory-container">
    <el-card class="search-card">
      <el-form :inline="true" :model="queryParams">
        <el-form-item label="商品名称">
          <el-input v-model="queryParams.keyword" placeholder="请输入商品名称" clearable @keyup.enter="handleSearch" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">搜索</el-button>
          <el-button @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card class="table-card">
      <template #header>
        <div class="card-header">
          <span>库存列表</span>
          <div>
            <el-button type="success" @click="handleStockIn">入库</el-button>
            <el-button type="warning" @click="handleStockOut">出库</el-button>
          </div>
        </div>
      </template>

      <el-table :data="tableData" v-loading="loading" border stripe>
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="productName" label="商品名称" />
        <el-table-column prop="warehouse" label="仓库" width="120" />
        <el-table-column prop="quantity" label="库存数量" width="100">
          <template #default="{ row }">
            <el-tag :type="row.quantity <= row.warningQuantity ? 'danger' : 'success'">
              {{ row.quantity }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="warningQuantity" label="预警数量" width="100" />
        <el-table-column prop="updateTime" label="更新时间" width="180" />
      </el-table>

      <el-pagination class="pagination" v-model:current-page="queryParams.pageNum"
        v-model:page-size="queryParams.pageSize" :page-sizes="[10, 20, 50]" :total="total"
        layout="total, sizes, prev, pager, next, jumper" @size-change="fetchData" @current-change="fetchData" />
    </el-card>

    <!-- 入库/出库对话框 -->
    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="500px">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="商品" prop="productId">
          <el-select
            v-model="form.productId"
            filterable
            remote
            reserve-keyword
            placeholder="请输入商品名称搜索"
            :remote-method="searchProducts"
            :loading="productLoading"
            @change="handleProductChange"
            style="width: 100%"
          >
            <el-option
              v-for="item in productOptions"
              :key="item.id"
              :label="item.name"
              :value="item.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="商品名称">
          <el-input v-model="form.productName" disabled />
        </el-form-item>
        <el-form-item label="操作时间">
          <el-input v-model="form.operationTime" disabled />
        </el-form-item>
        <el-form-item label="数量" prop="quantity">
          <el-input-number v-model="form.quantity" :min="1" />
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="form.remark" type="textarea" :rows="2" placeholder="请输入备注" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { listInventory, stockIn, stockOut } from '@/api/inventory'
import { getProductList } from '@/api/product'

const loading = ref(false)
const tableData = ref([])
const total = ref(0)
const dialogVisible = ref(false)
const dialogTitle = ref('')
const operationType = ref('in')
const formRef = ref(null)
const productLoading = ref(false)
const productOptions = ref([])

const queryParams = reactive({
  pageNum: 1,
  pageSize: 10,
  keyword: ''
})

const form = reactive({
  productId: null,
  productName: '',
  operationTime: '',
  quantity: 1,
  remark: ''
})

const rules = {
  productId: [{ required: true, message: '请选择商品', trigger: 'change' }],
  quantity: [{ required: true, message: '请输入数量', trigger: 'blur' }]
}

const fetchData = async () => {
  loading.value = true
  try {
    const res = await listInventory(queryParams)
    tableData.value = res.data.records
    total.value = res.data.total
  } finally {
    loading.value = false
  }
}

// 搜索商品
const searchProducts = async (query) => {
  if (query) {
    productLoading.value = true
    try {
      const res = await getProductList({ pageNum: 1, pageSize: 20, keyword: query })
      productOptions.value = res.data.records || []
    } catch (error) {
      console.error(error)
      productOptions.value = []
    } finally {
      productLoading.value = false
    }
  } else {
    productOptions.value = []
  }
}

// 商品选择变化
const handleProductChange = (productId) => {
  const product = productOptions.value.find(item => item.id === productId)
  form.productName = product ? product.name : ''
}

// 获取当前时间
const getCurrentTime = () => {
  const now = new Date()
  const year = now.getFullYear()
  const month = String(now.getMonth() + 1).padStart(2, '0')
  const day = String(now.getDate()).padStart(2, '0')
  const hours = String(now.getHours()).padStart(2, '0')
  const minutes = String(now.getMinutes()).padStart(2, '0')
  const seconds = String(now.getSeconds()).padStart(2, '0')
  return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`
}

const handleSearch = () => {
  queryParams.pageNum = 1
  fetchData()
}

const resetQuery = () => {
  queryParams.keyword = ''
  handleSearch()
}

const handleStockIn = () => {
  dialogTitle.value = '入库'
  operationType.value = 'in'
  Object.assign(form, { productId: null, productName: '', operationTime: getCurrentTime(), quantity: 1, remark: '' })
  productOptions.value = []
  dialogVisible.value = true
}

const handleStockOut = () => {
  dialogTitle.value = '出库'
  operationType.value = 'out'
  Object.assign(form, { productId: null, productName: '', operationTime: getCurrentTime(), quantity: 1, remark: '' })
  productOptions.value = []
  dialogVisible.value = true
}

const handleSubmit = async () => {
  await formRef.value.validate()
  const submitData = {
    productId: form.productId,
    quantity: form.quantity,
    remark: form.remark
  }
  if (operationType.value === 'in') {
    await stockIn(submitData)
    ElMessage.success('入库成功')
  } else {
    await stockOut(submitData)
    ElMessage.success('出库成功')
  }
  dialogVisible.value = false
  fetchData()
}

onMounted(fetchData)
</script>

<style scoped>
.inventory-container {
  padding: 20px;
}
.search-card {
  margin-bottom: 20px;
}
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.pagination {
  margin-top: 20px;
  justify-content: flex-end;
}
</style>
