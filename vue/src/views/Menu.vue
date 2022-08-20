<template>
<div>

  <div style="padding: 10px 0">
    <el-input style="width: 200px" placeholder="请输入名称" suffix-icon="el-icon-search" class="ml-5" v-model="name"></el-input>
    <!-- <el-input style="width: 200px" placeholder="请输入邮箱" suffix-icon="el-icon-message" class="ml-5" v-model="email"></el-input>
    <el-input style="width: 200px" placeholder="请输入地址" suffix-icon="el-icon-location" class="ml-5" v-model="address"></el-input> -->
    <el-button class="ml-5" type="primary" @click="load">搜索</el-button>
    <el-button type="warning" @click="reset">重置</el-button>
  </div>

  <div style="margin: 10px 0">
    <el-button type="primary" @click="handleAdd">新增<i class="el-icon-circle-plus-outline"></i> </el-button>
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
    <!--
    <el-upload action="http://localhost:9090/menu/import" :show-file-list="false" accept="xlsx" :on-success="handleExcelImportSuccess" style="display: inline-block">
    <el-button type="primary" class="ml-5">导入<i class="el-icon-bottom"></i></el-button>
    </el-upload>
    <el-button type="primary" class="ml-5" @click="exp">导出<i class="el-icon-top"></i> </el-button>
    -->
  </div>

  <el-table :data="tableData" border stripe :header-cell-class-name="headerBg" @selection-change="handleSelectionChange">
    <el-table-column type="selection" width="55"></el-table-column>
    <el-table-column prop="id" label="ID" width="80"></el-table-column>
    <el-table-column prop="name" label="名称"></el-table-column>
    <el-table-column prop="path" label="路径"></el-table-column>
    <el-table-column prop="icon" label="图标"></el-table-column>
    <el-table-column prop="description" label="描述"></el-table-column>
    <el-table-column label="操作" width="200" align="center">
      <template slot-scope="scope">
        <el-button type="warning" @click="handleUpdate(scope.row)">编辑<i class="el-icon-edit"></i></el-button>
        <el-popconfirm
            class="ml-5"
            confirm-button-text='确定'
            cancel-button-text='再想想吧'
            icon="el-icon-info"
            icon-color="red"
            title="您确定删除吗？"
            @confirm="handleDelete(scope.row.id)"
        >
          <el-button type="danger" slot="reference">删除<i class="el-icon-delete"></i> </el-button>
        </el-popconfirm>
      </template>
    </el-table-column>
  </el-table>
  <div style="padding: 10px 0">
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

  <el-dialog title="角色信息" :visible.sync="dialogFormVisible" width="40%">
    <el-form label-width="80px" size="small">
      <el-form-item label="名称">
        <el-input v-model="form.name" autocomplete="off"></el-input>
      </el-form-item>
      <el-form-item label="路径">
        <el-input v-model="form.path" autocomplete="off"></el-input>
      </el-form-item>
      <el-form-item label="图标">
        <el-input v-model="form.icon" autocomplete="off"></el-input>
      </el-form-item>
      <el-form-item label="描述">
        <el-input v-model="form.description" autocomplete="off"></el-input>
      </el-form-item>
    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button @click="dialogFormVisible = false">取 消</el-button>
      <el-button type="primary" @click="save">确 定</el-button>
    </div>
  </el-dialog>

</div>
</template>

<script>
export default {
  name: "User",
  data() {
    return {
      tableData: [],
      total: 0,
      pageNo: 1,
      pageSize: 3,
      name: "",
      form: {},
      dialogFormVisible: false,
      multipleSelection: [],
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
      this.request.get("/menu/pageSelect",{
        params: {
          pageNo: this.pageNo,
          pageSize: this.pageSize,
          name: this.name,
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
    handleAdd() {
      this.dialogFormVisible = true
      this.form = {}
    },
    handleUpdate(row) {
      this.form = row
      this.dialogFormVisible = true
    },
    handleDelete(id) {
      this.request.delete("/menu/delete/" + id).then(res => {
        if (res.code === '200') {
          this.$message.success("删除成功!")
          this.load()
        } else {
          this.$message.error("删除失败!")
        }
      })
    },
    save() {
      // this.form是传递的参数
      this.request.post("/menu/saveOrUpdate", this.form).then(res => {
        if (res.code === '200') {
          this.$message.success("用户保存成功!")
          this.dialogFormVisible = false
          this.load()
        } else {
          this.$message.error("用户保存失败!")
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
      this.request.post("/menu/batchDelete", ids).then(res => {
        if (res.code === '200') {
          this.$message.success("批量删除成功!")
          this.load()
        } else {
          this.$message.error("批量删除失败!")
        }
      })
    },
    exp() {
      window.open("http://localhost:9090/menu/export")
    },
    handleExcelImportSuccess() {
      this.$message.success("导入成功")
      this.load()
    }
  }
}
</script>

<style>
.headerBg {
  background: #eee!important;
}
</style>