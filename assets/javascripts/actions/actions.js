export function refreshUsers(users) {
    return {
        type: "REFRESH_USERS",
        value: users,
    }
}