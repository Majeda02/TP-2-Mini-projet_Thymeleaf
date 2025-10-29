import React from 'react';
import { Link } from 'react-router-dom';
import { Navbar, Nav, Container } from 'react-bootstrap';

const Navigation = () => {
  return (
    <Navbar expand="lg" variant="dark" className="navbar-gradient">
      <Container>
        <Navbar.Brand as={Link} to="/">
          <i className="fas fa-hotel"></i> Hôtel
        </Navbar.Brand>
        <Navbar.Toggle aria-controls="basic-navbar-nav" />
        <Navbar.Collapse id="basic-navbar-nav">
          <Nav className="ms-auto">
            <Nav.Link as={Link} to="/chambres">Chambres</Nav.Link>
            <Nav.Link as={Link} to="/clients">Clients</Nav.Link>
            <Nav.Link as={Link} to="/reservations">Réservations</Nav.Link>
            <Nav.Link as={Link} to="/statistiques">Statistiques</Nav.Link>
          </Nav>
        </Navbar.Collapse>
      </Container>
    </Navbar>
  );
};

export default Navigation;




