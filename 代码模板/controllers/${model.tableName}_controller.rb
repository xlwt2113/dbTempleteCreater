class ${model.objNames}Controller < ApplicationController
  before_action :set_${model.slName}, only: [:show, :edit, :update, :destroy,:showall]
  before_action :set_search, only: [:index]

  def set_search
    #获取查询参数
#foreach($key in $model.allColumn) #if($key.fieldName!='vcId'&&$key.fieldName!='id'&&$key.queryCol==true)
    @${key.name}_sc = params[:${key.name}_sc]
#end #end
  end

  # GET /${model.tableName}
  # GET /${model.tableName}.json
  def index
    #组合查询条件
    @query = ${model.objName}.all();
#foreach($key in $model.allColumn) #if($key.fieldName!='vcId'&&$key.fieldName!='id'&&$key.queryCol==true)
    @query = @query.where("${key.name} = ?",params[:${key.name}_sc]) unless params[:${key.name}_sc].blank?
#end #end

    @${model.tableName} = @query.order(id: :asc).paginate(page:params[:page],per_page:20)
    #形成模板
    respond_to do |format|
      format.html
      format.xls
      end
  end

  # GET /${model.tableName}/1
  # GET /${model.tableName}/1.json
  def show
  end

  def showall
      respond_to do |format|
      format.html   
      format.pdf{
        render pdf: params[:id].to_s
      }
    end   
  end

  # GET /${model.tableName}/new
  def new
    @${model.slName} = ${model.objName}.new
  end

  # GET /${model.tableName}/1/edit
  def edit
  end

  # POST /${model.tableName}
  # POST /${model.tableName}.json
  def create
    @${model.slName} = ${model.objName}.new(${model.slName}_params)

    respond_to do |format|
      if @${model.slName}.save
        format.html { redirect_to ${model.tableName}_path, notice: '数据新增成功.' }
        format.json { render :show, status: :created, location: @${model.slName} }
      else
        format.html { render :new }
        format.json { render json: @${model.slName}.errors, status: :unprocessable_entity }
      end
    end
  end

  # PATCH/PUT /${model.tableName}/1
  # PATCH/PUT /${model.tableName}/1.json
  def update
    respond_to do |format|
      if @${model.slName}.update(${model.slName}_params)
        format.html { redirect_to ${model.tableName}_path, notice: '数据修改成功.' }
        format.json { render :show, status: :ok, location: @${model.slName} }
      else
        format.html { render :edit }
        format.json { render json: @${model.slName}.errors, status: :unprocessable_entity }
      end
    end
  end

  # DELETE /${model.tableName}/1
  # DELETE /${model.tableName}/1.json
  def destroy
    @${model.slName}.destroy
    respond_to do |format|
      format.html { redirect_to ${model.tableName}_url, notice: '数据删除成功.' }
      format.json { head :no_content }
    end
  end

  private
    # Use callbacks to share common setup or constraints between actions.
    def set_${model.slName}
      @${model.slName} = ${model.objName}.find(params[:id])
    end

    # Never trust parameters from the scary internet, only allow the white list through.
    def ${model.slName}_params
      params.require(:${model.tableDsName}).permit(#foreach($key in $model.columnList):$key.name#if($foreach.hasNext), #end#end)
    end
end
