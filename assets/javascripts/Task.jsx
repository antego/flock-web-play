import React from "react";
import { Button, ButtonGroup } from 'react-bootstrap';

export default class Task extends React.Component {
    handleNewOrder() {
        this.props.submitOrder({
            fromPnt: this.props.fromPnt,
            toPnt: this.props.toPnt});
    }

    render() {
        let fromPntCoords;
        let toPntCoords;
        if (this.props.fromPnt) {
            fromPntCoords = (<p> {this.props.fromPnt.lat}  {this.props.fromPnt.lng}</p>);
        }
        if (this.props.toPnt) {
            toPntCoords = (<p> {this.props.toPnt.lat}  {this.props.toPnt.lng}</p>);
        }
        return (<div>
            <ButtonGroup>
                <Button active={this.props.pickFromPnt} onClick={this.props.pickFromPntCb}>Pick origin point</Button>
                <Button active={this.props.pickToPnt} onClick={this.props.pickToPntCb}>Pick destination point</Button>
            </ButtonGroup>
            {fromPntCoords}
            <br/>
            {toPntCoords}
            <Button className="btn-success" onClick={::this.handleNewOrder}>Submit new order</Button>
        </div>);
    }
}