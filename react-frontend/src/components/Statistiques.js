import React, { useState, useEffect } from 'react';
import { Card, Row, Col, Form, Button } from 'react-bootstrap';
import {
  Chart as ChartJS,
  CategoryScale,
  LinearScale,
  BarElement,
  ArcElement,
  Title,
  Tooltip,
  Legend,
} from 'chart.js';
import { Doughnut, Bar } from 'react-chartjs-2';
import axios from 'axios';

ChartJS.register(
  CategoryScale,
  LinearScale,
  BarElement,
  ArcElement,
  Title,
  Tooltip,
  Legend
);

const API_URL = 'http://localhost:8080';

const Statistiques = () => {
  const [stats, setStats] = useState({
    tauxOccupation: 0,
    revPAR: 0,
    revenuTotal: 0,
    reservationsByStatut: {},
    chambresByType: {}
  });
  const [dateDebut, setDateDebut] = useState('');
  const [dateFin, setDateFin] = useState('');

  useEffect(() => {
    fetchStats();
  }, []);

  const fetchStats = async () => {
    try {
      const params = new URLSearchParams();
      if (dateDebut) params.append('dateDebut', dateDebut);
      if (dateFin) params.append('dateFin', dateFin);

      const response = await axios.get(`${API_URL}/statistiques/api/data?${params.toString()}`);
      setStats(response.data);
    } catch (error) {
      console.error('Error fetching stats:', error);
    }
  };

  const statutChartData = {
    labels: Object.keys(stats.reservationsByStatut),
    datasets: [
      {
        data: Object.values(stats.reservationsByStatut),
        backgroundColor: ['#28a745', '#ffc107', '#dc3545', '#17a2b8']
      }
    ]
  };

  const typeChartData = {
    labels: Object.keys(stats.chambresByType),
    datasets: [
      {
        label: 'Nombre de chambres',
        data: Object.values(stats.chambresByType),
        backgroundColor: '#667eea'
      }
    ]
  };

  return (
    <div>
      <h2 className="mb-4">
        <i className="fas fa-chart-line"></i> Tableau de Bord
      </h2>

      <Card className="mb-4">
        <Card.Body>
          <Form>
            <Row className="g-3">
              <Col md={5}>
                <Form.Label>Date Début</Form.Label>
                <Form.Control
                  type="date"
                  value={dateDebut}
                  onChange={(e) => setDateDebut(e.target.value)}
                />
              </Col>
              <Col md={5}>
                <Form.Label>Date Fin</Form.Label>
                <Form.Control
                  type="date"
                  value={dateFin}
                  onChange={(e) => setDateFin(e.target.value)}
                />
              </Col>
              <Col md={2} className="d-flex align-items-end">
                <Button variant="secondary" onClick={fetchStats} className="w-100">
                  <i className="fas fa-filter"></i> Filtrer
                </Button>
              </Col>
            </Row>
          </Form>
        </Card.Body>
      </Card>

      <Row className="g-4 mb-4">
        <Col md={4}>
          <Card style={{ borderRadius: '15px', padding: '25px' }}>
            <div className="d-flex justify-content-between align-items-center">
              <div>
                <i
                  className="fas fa-bed"
                  style={{ fontSize: '48px', opacity: 0.2, color: '#3498db' }}
                ></i>
                <div className="mt-3">
                  <h6 className="text-muted mb-0">Taux d'Occupation</h6>
                  <div style={{ fontSize: '2.5rem', fontWeight: 'bold', color: '#667eea' }}>
                    {stats.tauxOccupation.toFixed(1)}%
                  </div>
                </div>
              </div>
            </div>
          </Card>
        </Col>

        <Col md={4}>
          <Card style={{ borderRadius: '15px', padding: '25px' }}>
            <div className="d-flex justify-content-between align-items-center">
              <div>
                <i
                  className="fas fa-money-bill-wave"
                  style={{ fontSize: '48px', opacity: 0.2, color: '#2ecc71' }}
                ></i>
                <div className="mt-3">
                  <h6 className="text-muted mb-0">RevPAR</h6>
                  <div style={{ fontSize: '2.5rem', fontWeight: 'bold', color: '#667eea' }}>
                    {stats.revPAR.toFixed(2)} DH
                  </div>
                </div>
              </div>
            </div>
          </Card>
        </Col>

        <Col md={4}>
          <Card style={{ borderRadius: '15px', padding: '25px' }}>
            <div className="d-flex justify-content-between align-items-center">
              <div>
                <i
                  className="fas fa-coins"
                  style={{ fontSize: '48px', opacity: 0.2, color: '#f39c12' }}
                ></i>
                <div className="mt-3">
                  <h6 className="text-muted mb-0">Revenu Total</h6>
                  <div style={{ fontSize: '2.5rem', fontWeight: 'bold', color: '#667eea' }}>
                    {stats.revenuTotal.toFixed(2)} DH
                  </div>
                </div>
              </div>
            </div>
          </Card>
        </Col>
      </Row>

      <Row>
        <Col md={6}>
          <Card style={{ borderRadius: '15px', padding: '25px' }}>
            <h5 className="mb-3">
              <i className="fas fa-chart-pie"></i> Réservations par Statut
            </h5>
            <Doughnut data={statutChartData} />
          </Card>
        </Col>

        <Col md={6}>
          <Card style={{ borderRadius: '15px', padding: '25px' }}>
            <h5 className="mb-3">
              <i className="fas fa-chart-bar"></i> Répartition des Chambres par Type
            </h5>
            <Bar
              data={typeChartData}
              options={{
                scales: {
                  y: {
                    beginAtZero: true
                  }
                }
              }}
            />
          </Card>
        </Col>
      </Row>
    </div>
  );
};

export default Statistiques;

