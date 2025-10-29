import React, { useState, useEffect } from 'react';
import { Card, Button, Form, Table, Modal, Alert } from 'react-bootstrap';
import axios from 'axios';

const API_URL = 'http://localhost:8080/api';

const Reservations = () => {
  const [reservations, setReservations] = useState([]);
  const [chambres, setChambres] = useState([]);
  const [clients, setClients] = useState([]);
  const [showModal, setShowModal] = useState(false);
  const [formData, setFormData] = useState({
    chambreId: '',
    clientId: '',
    checkIn: '',
    checkOut: '',
    statut: '',
    total: ''
  });
  const [message, setMessage] = useState({ type: '', text: '' });

  useEffect(() => {
    fetchReservations();
    fetchChambres();
    fetchClients();
  }, []);

  const fetchReservations = async () => {
    try {
      const response = await axios.get(`${API_URL}/reservations`);
      setReservations(response.data);
    } catch (error) {
      console.error('Error fetching reservations:', error);
    }
  };

  const fetchChambres = async () => {
    try {
      const response = await axios.get(`${API_URL}/chambres`);
      setChambres(response.data);
    } catch (error) {
      console.error('Error fetching chambres:', error);
    }
  };

  const fetchClients = async () => {
    try {
      const response = await axios.get(`${API_URL}/clients`);
      setClients(response.data);
    } catch (error) {
      console.error('Error fetching clients:', error);
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      await axios.post(`${API_URL}/reservations`, formData);
      setMessage({ type: 'success', text: 'Réservation créée avec succès!' });
      setShowModal(false);
      fetchReservations();
      resetForm();
    } catch (error) {
      setMessage({ type: 'danger', text: 'Erreur: ' + error.message });
    }
  };

  const handleDelete = async (chambreId, clientId) => {
    if (window.confirm('Êtes-vous sûr de vouloir supprimer cette réservation?')) {
      try {
        await axios.delete(`${API_URL}/reservations/${chambreId}/${clientId}`);
        setMessage({ type: 'success', text: 'Réservation supprimée avec succès!' });
        fetchReservations();
      } catch (error) {
        setMessage({ type: 'danger', text: 'Erreur: ' + error.message });
      }
    }
  };

  const resetForm = () => {
    setFormData({
      chambreId: '',
      clientId: '',
      checkIn: '',
      checkOut: '',
      statut: '',
      total: ''
    });
  };

  const getStatusBadgeClass = (statut) => {
    if (statut === 'Confirmée') return 'bg-success';
    if (statut === 'Annulée') return 'bg-danger';
    return 'bg-warning';
  };

  return (
    <div>
      <div className="d-flex justify-content-between align-items-center mb-4">
        <h2>
          <i className="fas fa-calendar-check"></i> Gestion des Réservations
        </h2>
        <Button variant="primary" onClick={() => { resetForm(); setShowModal(true); }}>
          <i className="fas fa-plus"></i> Nouvelle Réservation
        </Button>
      </div>

      {message.text && (
        <Alert variant={message.type} dismissible onClose={() => setMessage({ type: '', text: '' })}>
          {message.text}
        </Alert>
      )}

      <Card>
        <Card.Body>
          <div className="table-responsive">
            <Table hover>
              <thead>
                <tr>
                  <th>Chambre</th>
                  <th>Client</th>
                  <th>Check-In</th>
                  <th>Check-Out</th>
                  <th>Statut</th>
                  <th>Total</th>
                  <th>Actions</th>
                </tr>
              </thead>
              <tbody>
                {reservations.map((reservation, index) => (
                  <tr key={index}>
                    <td>
                      <i className="fas fa-door-open"></i> {reservation.chambre?.numero}
                      <br />
                      <small className="text-muted">{reservation.chambre?.type}</small>
                    </td>
                    <td>
                      <i className="fas fa-user"></i> {reservation.client?.nom}
                    </td>
                    <td>{new Date(reservation.PK?.checkIn).toLocaleDateString('fr-FR')}</td>
                    <td>{new Date(reservation.PK?.checkOut).toLocaleDateString('fr-FR')}</td>
                    <td>
                      <span className={`badge ${getStatusBadgeClass(reservation.statut)}`}>
                        {reservation.statut}
                      </span>
                    </td>
                    <td style={{ color: '#28a745', fontWeight: 'bold' }}>
                      {reservation.total} DH
                    </td>
                    <td>
                      <Button
                        variant="outline-danger"
                        size="sm"
                        onClick={() => handleDelete(reservation.PK?.chambre, reservation.PK?.client)}
                      >
                        <i className="fas fa-trash"></i>
                      </Button>
                    </td>
                  </tr>
                ))}
              </tbody>
            </Table>
          </div>
        </Card.Body>
      </Card>

      <Modal show={showModal} onHide={() => setShowModal(false)}>
        <Modal.Header closeButton>
          <Modal.Title>Nouvelle Réservation</Modal.Title>
        </Modal.Header>
        <Form onSubmit={handleSubmit}>
          <Modal.Body>
            <Form.Group className="mb-3">
              <Form.Label>Chambre *</Form.Label>
              <Form.Select
                value={formData.chambreId}
                onChange={(e) => setFormData({ ...formData, chambreId: e.target.value })}
                required
              >
                <option value="">Sélectionner une chambre</option>
                {chambres.map((chambre) => (
                  <option key={chambre.id} value={chambre.id}>
                    {chambre.numero} ({chambre.type}) - {chambre.prixNuit} DH/nuit
                  </option>
                ))}
              </Form.Select>
            </Form.Group>
            <Form.Group className="mb-3">
              <Form.Label>Client *</Form.Label>
              <Form.Select
                value={formData.clientId}
                onChange={(e) => setFormData({ ...formData, clientId: e.target.value })}
                required
              >
                <option value="">Sélectionner un client</option>
                {clients.map((client) => (
                  <option key={client.id} value={client.id}>
                    {client.nom}
                  </option>
                ))}
              </Form.Select>
            </Form.Group>
            <div className="row">
              <div className="col-md-6">
                <Form.Group className="mb-3">
                  <Form.Label>Check-In *</Form.Label>
                  <Form.Control
                    type="date"
                    value={formData.checkIn}
                    onChange={(e) => setFormData({ ...formData, checkIn: e.target.value })}
                    required
                  />
                </Form.Group>
              </div>
              <div className="col-md-6">
                <Form.Group className="mb-3">
                  <Form.Label>Check-Out *</Form.Label>
                  <Form.Control
                    type="date"
                    value={formData.checkOut}
                    onChange={(e) => setFormData({ ...formData, checkOut: e.target.value })}
                    required
                  />
                </Form.Group>
              </div>
            </div>
            <Form.Group className="mb-3">
              <Form.Label>Statut *</Form.Label>
              <Form.Select
                value={formData.statut}
                onChange={(e) => setFormData({ ...formData, statut: e.target.value })}
                required
              >
                <option value="">Sélectionner un statut</option>
                <option value="Confirmée">Confirmée</option>
                <option value="En attente">En attente</option>
                <option value="Annulée">Annulée</option>
              </Form.Select>
            </Form.Group>
            <Form.Group className="mb-3">
              <Form.Label>Total (DH) *</Form.Label>
              <Form.Control
                type="number"
                step="0.01"
                value={formData.total}
                onChange={(e) => setFormData({ ...formData, total: e.target.value })}
                required
              />
            </Form.Group>
          </Modal.Body>
          <Modal.Footer>
            <Button variant="secondary" onClick={() => setShowModal(false)}>
              Annuler
            </Button>
            <Button variant="primary" type="submit">
              Enregistrer
            </Button>
          </Modal.Footer>
        </Form>
      </Modal>
    </div>
  );
};

export default Reservations;

