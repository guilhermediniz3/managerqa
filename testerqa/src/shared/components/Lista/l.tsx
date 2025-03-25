




import React from "react";
import { ListGroup, Card } from "react-bootstrap";

const Listagem = ({ titulo, items, renderItem }) => {
  return (
    <Card>
      {titulo && <Card.Header>{titulo}</Card.Header>}
      <ListGroup variant="flush">
        {items.map((item, index) => (
          <ListGroup.Item key={index}>
            {renderItem(item)}
          </ListGroup.Item>
        ))}
      </ListGroup>
    </Card>
  );
};

export default Listagem;