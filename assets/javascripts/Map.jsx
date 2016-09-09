import React from 'react';
import { render } from 'react-dom';
import {
    Circle,
    FeatureGroup,
    LayerGroup,
    LayersControl,
    Map,
    Marker,
    Popup,
    Rectangle,
    TileLayer,
} from 'react-leaflet';
import { connect } from "react-redux"
import * as act from "./actions/actions"

const position = [51.505, -0.09];
@connect((store) => {
    return {
        users: store.users,
    };
})
export default class MyMap extends React.Component {
    // handleMapClick(e) {
    //     if (this.props.pickMode === "PICK_FROM") {
    //         this.props.dispatch(act.fromPointSelected(e.latlng));
    //     } else if (this.props.pickMode === "PICK_TO") {
    //         this.props.dispatch(act.toPointSelected(e.latlng));
    //     }
    // }


    render() {
        // let fromMarker = this.props.fromPoint ? <Marker position={this.props.fromPoint} /> : null;
        // let toMarker = this.props.toPoint ? <Marker position={this.props.toPoint} /> : null;
        return (<Map center={position} zoom={13} onClick={::this.handleMapClick}>
                    <TileLayer
                        url='http://{s}.tile.osm.org/{z}/{x}/{y}.png'
                        attribution='&copy; <a href="http://osm.org/copyright">OpenStreetMap</a> contributors'
                    />
                    <LayerGroup>
                        {this.props.users.map((user, i) => {
                            let position = {lat: user.lat, lng: user.lng};
                            return <Marker position={position} />;
                        })}
                    </LayerGroup>
                </Map>);
    }
}



