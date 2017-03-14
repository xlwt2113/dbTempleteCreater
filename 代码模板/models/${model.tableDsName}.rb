class ${model.objName} < ActiveRecord::Base
	#foreach($key in $model.allColumn) #if($key.fieldName!='vcId'&&$key.fieldName!='id'&&$key.isNull!=true)
    validates_presence_of   :${key.name},  :message => "${key.comment}不能为空!"
	#end #end
end
