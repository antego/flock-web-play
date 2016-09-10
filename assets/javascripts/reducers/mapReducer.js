export default function reducer(state={
    refreshPeriodMillis: 1000,
    users: [],
    userTraces: new Map(),
}, action) {

    switch (action.type) {
        case "REFRESH_USERS": {
            let traces = state.userTraces;
            action.value.forEach((user) => {
                let trace = traces.get(user.name);
                if (!trace) {
                    trace = [];
                }
                let lastLatLng = trace.length > 0 ? trace[trace.length - 1] : null;
                if (trace.length === 0 || lastLatLng.lat !== user.lat || lastLatLng.lng !== user.lng) {
                    trace.push({lat: user.lat, lng: user.lng});
                }
            });

            return {...state, users: action.value, userTraces: traces}
        }
        case "SET_REFRESH_PERIOD": {
            return {...state, refreshPeriodMillis: action.value}
        }
    }

    return state
}