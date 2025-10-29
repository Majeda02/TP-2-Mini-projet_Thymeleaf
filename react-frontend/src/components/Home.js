import React from 'react';
import { Link } from 'react-router-dom';
import { Row, Col, Card } from 'react-bootstrap';

const Home = () => {
  const menuItems = [
    {
      title: 'Chambres',
      icon: 'fas fa-door-open',
      color: '#3498db',
      path: '/chambres'
    },
    {
      title: 'Clients',
      icon: 'fas fa-users',
      color: '#2ecc71',
      path: '/clients'
    },
    {
      title: 'Réservations',
      icon: 'fas fa-calendar-check',
      color: '#e74c3c',
      path: '/reservations'
    },
    {
      title: 'Statistiques',
      icon: 'fas fa-chart-line',
      color: '#f39c12',
      path: '/statistiques'
    }
  ];

  return (
    <div className="text-center" style={{ padding: '40px 0' }}>
      <Card className="mb-4" style={{ borderRadius: '15px', padding: '40px' }}>
        <Card.Body>
          <h1>
            <i className="fas fa-hotel"></i> Gestion d'Hôtel
          </h1>
          <p className="text-muted" style={{ fontSize: '18px' }}>
            Système de gestion des chambres et réservations
          </p>
        </Card.Body>
      </Card>

      <Row className="g-4">
        {menuItems.map((item, index) => (
          <Col key={index} md={6} lg={3}>
            <Link
              to={item.path}
              style={{ textDecoration: 'none', color: 'inherit' }}
            >
              <Card className="text-center h-100 card-hover" style={{ cursor: 'pointer' }}>
                <Card.Body style={{ padding: '30px' }}>
                  <i
                    className={item.icon}
                    style={{ fontSize: '48px', color: item.color, marginBottom: '20px' }}
                  ></i>
                  <h5>{item.title}</h5>
                  <p className="text-muted">Gérer les {item.title.toLowerCase()}</p>
                </Card.Body>
              </Card>
            </Link>
          </Col>
        ))}
      </Row>
    </div>
  );
};

export default Home;




