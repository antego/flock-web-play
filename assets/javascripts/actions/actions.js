export function registerUser(user) {
    //todo send to server
    return {
        type: "REGISTER_USER",
        value: user,
    }
}

export function refreshUsers(users) {
    return {
        type: "REFRESH_USERS",
        value: users,
    }
}

export function setRefreshInterval(intervalMillis) {
    return {
        type: "SET_REFRESH_PERIOD",
        value: intervalMillis,
    }
}