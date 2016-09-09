import { combineReducers } from "redux"

import newTask from "./newTaskReducer"
import login from "./loginReducer"

export default combineReducers({
    newTask,
    login,
})