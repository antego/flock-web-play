import { combineReducers } from "redux"

import newTask from "./newTaskReducer"
import login from "./loginReducer"
import map from "./mapReducer"

export default combineReducers({
    newTask,
    login,
    map,
})