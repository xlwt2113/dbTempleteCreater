
  resources :${model.name}

  get "/${model.name}/showall/:id"=>"${model.name}#showall"
