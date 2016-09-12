export default function reducer(state={
    refreshPeriodMillis: 1000,
    users: [],
    userTraces: new Map(),
}, action) {

    switch (action.type) {
        case "REFRESH_USERS": {
            let traces = state.userTraces;
            action.value.forEach((user) => {
                let newLat = Number.parseFloat(user.lat);
                let newLng = Number.parseFloat(user.lng);
                if (Number.isNaN(newLat) || Number.isNaN(newLng)) {
                    return;
                }
                let trace = traces.get(user.name);
                if (!trace) {
                    trace = [];
                    traces.set(user.name, trace);
                }
                let lastLatLng = trace.length > 0 ? trace[trace.length - 1] : null;
                if (trace.length === 0 || lastLatLng[0] !== newLat || lastLatLng[1] !== newLng) {
                    trace.push([newLat, newLng]);
                }

            });

            return {...state, users: action.value, userTraces: traces}
        }
    }

    return state
}