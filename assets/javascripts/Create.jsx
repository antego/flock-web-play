import React from "react";
import { Button, ButtonGroup } from 'react-bootstrap';
import { connect } from "react-redux"
import * as act from "./actions/actions"

//todo from react-bootstrap to plain bs
@connect((store) => {
    return {
        fromPoint: store.fromPoint,
        toPoint: store.toPoint,
        pickMode: store.pickMode,
        newOrder: store.newOrder
    };
})
export default class Create extends React.Component {
    pickFrom() {
        this.props.dispatch(act.setPickFromMode())
    }

    pickTo() {
        this.props.dispatch(act.setPickToMode())
    }

    handleNewOrder() {
        this.props.dispatch(act.setPickToMode())
    }

    render() {
        let fromLatLng = this.props.fromPnt;
        let toLatLng = this.props.toPnt;
        let fromPntCoords;
        let toPntCoords;
        if (fromLatLng) {
            fromPntCoords = (<p> {fromLatLng.lat}  {fromLatLng.lng}</p>);
        }
        if (toLatLng) {
            toPntCoords = (<p> {toLatLng.lat}  {toLatLng.lng}</p>);
        }
        return (<div>
            <ButtonGroup>
                <Button active={this.props.pickMode === "PICK_TO"} onClick={::this.pickFrom}>Pick origin point</Button>
                <Button active={this.props.pickMode === "PICK_FROM"} onClick={::this.pickTo}>Pick destination point</Button>
            </ButtonGroup>
            {fromPntCoords}
            <br/>
            {toPntCoords}
            <Button className="btn-success" onClick={::this.handleNewOrder}>Submit new order</Button>
        </div>);
    }
}