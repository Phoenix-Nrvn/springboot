<template>
<div>

  <div style="padding: 10px 0">
    <el-input style="width: 200px" placeholder="请输入文件名称" suffix-icon="el-icon-search" class="ml-5" v-model="name"></el-input>
    <!-- <el-input style="width: 200px" placeholder="请输入文件类型" suffix-icon="el-icon-search" class="ml-5" v-model="type"></el-input> -->
    <el-button class="ml-5" type="primary" @click="load">搜索</el-button>
    <el-button type="warning" @click="reset">重置</el-button>
  </div>
  <div style="margin: 10px 0">
    <el-upload action="http://localhost:9090/file/upload" :show-file-list="false" accept="xlsx" :on-success="handleFileUploadSuccess" style="display: inline-block">
      <el-button type="primary" class="ml-5">上传文件<i class="el-icon-bottom"></i> </el-button>
    </el-upload>
    <el-popconfirm
        class="ml-5"
        confirm-button-text='确定'
        cancel-button-text='再想想吧'
        icon="el-icon-info"
        icon-color="red"
        title="您确定删除吗？"
        @confirm="delBatch"
    >
      <el-button type="danger" slot="reference">批量删除<i class="el-icon-remove-outline"></i></el-button>
    </el-popconfirm>
  </div>

  <el-table :data="tableData" border stripe :header-cell-class-name="headerBg" @selection-change="handleSelectionChange">
    <div style="padding: 10px 0">
    </div>
    <el-table-column type="selection" width="55"></el-table-column>
    <el-table-column prop="id" label="ID" width="80"></el-table-column>
    <el-table-column prop="name" label="文件名称" ></el-table-column>
    <el-table-column prop="type" label="文件类型" ></el-table-column>
    <el-table-column prop="size" label="文件大小(kb)"></el-table-column>
    <el-table-column label="下载">
      <template slot-scope="scope">
        <el-button type="primary" @click="download(scope.row.url)">下载</el-button>
      </template>
    </el-table-column>
    <el-table-column label="启用">
      <template slot-scope="scope">
        <el-switch v-model="scope.row.enable" active-color="#13ce66" inactive-color="#ccc" @change="changeEnable(scope.row)"></el-switch>
      </template>
    </el-table-column>
    <el-table-column label="操作" width="200" align="center">
      <template slot-scope="scope">
        <el-popconfirm
            class="ml-5"
            confirm-button-text='确定'
            cancel-button-text='再想想吧'
            icon="el-icon-info"
            icon-color="red"
            title="您确定删除吗？"
            @confirm="handleDelete(scope.row.id)"
        >
          <el-button type="danger" slot="reference"> 删除 <i class="el-icon-delete"></i> </el-button>
        </el-popconfirm>
      </template>
    </el-table-column>
  </el-table>

  <el-pagination
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
      :current-page="pageNo"
      :page-sizes="[2, 3, 4, 5, 10, 20]"
      :page-size="pageSize"
      layout="total, sizes, prev, pager, next, jumper"
      :total="total">
  </el-pagination>
  </div>
</template>

<script>
export default {
  name: "Files",
  data() {
    return {
      tableData: [],
      name: '',
      type: '',
      multipleSelection: '',
      total: 0,
      pageNo: 1,
      pageSize: 3,
      headerBg: 'headerBg'
    }
  },
  created() {
    this.load()
  },
  methods: {
    handleSizeChange(pageSize) {
      console.log('每页 ${pageSize} 条');
      this.pageSize = pageSize
      this.load()
    },
    handleCurrentChange(pageNo) {
      console.log('当前页：${pageNo} 条')
      this.pageNo = pageNo
      this.load()
    },
    load() {
      this.request.get("/file/pageSelect",{
        params: {
          pageNo: this.pageNo,
          pageSize: this.pageSize,
          name: this.name
        }
      }).then(res => {
        console.log(res.data)

        this.tableData = res.data.records
        this.total = res.data.total
      })
    },
    reset() {
      this.name=""
      this.load()
    },
    handleDelete(id) {
      this.request.delete("/file/delete/" + id).then(res => {
        if (res.code === '200') {
          this.$message.success("删除成功!")
          this.load()
        } else {
          this.$message.error("删除失败!")
        }
      })
    },
    handleSelectionChange(val) {
      console.log(val)
      this.multipleSelection = val
    },
    delBatch() {
      // 将一个对象的数组变为一个纯id的数组
      let ids = this.multipleSelection.map(v => v.id)
      this.request.post("/file/batchDelete", ids).then(res => {
        if (res.code === '200') {
          this.$message.success("批量删除成功!")
          this.load()
        } else {
          this.$message.error("批量删除失败!")
        }
      })
    },
    handleFileUploadSuccess(res) {
      console.log(res)
    },
    download(url) {
      window.open(url)
    },
    changeEnable(row) {
      this.request.post("/file/update", row).then(res => {
        if (res.code === "200") {
          this.$message.success("操作成功")
          this.load()
        } else {
          this.$message.error("更新失败")
        }
      })
    }
  }
}
</script>

<style scoped>

</style>