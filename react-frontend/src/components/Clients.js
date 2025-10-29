import React, { useState, useEffect } from 'react';
import { Card, Row, Col, Button, Form, Modal, Alert } from 'react-bootstrap';
import axios from 'axios';

const API_URL = 'http://localhost:8080/api';

const Clients = () => {
  const [clients, setClients] = useState([]);
  const [showModal, setShowModal] = useState(false);
  const [editingClient, setEditingClient] = useState(null);
  const [formData, setFormData] = useState({
    nom: '',
    passeport: ''
  });
  const [message, setMessage] = useState({ type: '', text: '' });

  useEffect(() => {
    fetchClients();
  }, []);
  // eslint-disable-next-line

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
      if (editingClient) {
        await axios.put(`${API_URL}/clients/${editingClient.id}`, formData);
        setMessage({ type: 'success', text: 'Client mis à jour avec succès!' });
      } else {
        await axios.post(`${API_URL}/clients`, formData);
        setMessage({ type: 'success', text: 'Client créé avec succès!' });
      }

      setShowModal(false);
      fetchClients();
      resetForm();
    } catch (error) {
      setMessage({ type: 'danger', text: 'Erreur: ' + error.message });
    }
  };

  const handleEdit = (client) => {
    setEditingClient(client);
    setFormData({
      nom: client.nom,
      passeport: client.passeport || ''
    });
    setShowModal(true);
  };

  const handleDelete = async (id) => {
    if (window.confirm('Êtes-vous sûr de vouloir supprimer ce client?')) {
      try {
        await axios.delete(`${API_URL}/clients/${id}`);
        setMessage({ type: 'success', text: 'Client supprimé avec succès!' });
        fetchClients();
      } catch (error) {
        setMessage({ type: 'danger', text: 'Erreur: ' + error.message });
      }
    }
  };

  const resetForm = () => {
    setFormData({ nom: '', passeport: '' });
    setEditingClient(null);
  };

  return (
    <div>
      <div className="d-flex justify-content-between align-items-center mb-4">
        <h2>
          <i className="fas fa-users"></i> Gestion des Clients
        </h2>
        <Button variant="primary" onClick={() => { resetForm(); setShowModal(true); }}>
          <i className="fas fa-plus"></i> Nouveau Client
        </Button>
      </div>

      {message.text && (
        <Alert variant={message.type} dismissible onClose={() => setMessage({ type: '', text: '' })}>
          {message.text}
        </Alert>
      )}

      <Row className="g-4">
        {clients.map((client) => (
          <Col key={client.id} md={6} lg={4}>
            <Card className="h-100">
              <Card.Body>
                <h5 className="card-title mb-3">
                  <i className="fas fa-user"></i> {client.nom}
                </h5>
                <p className="text-muted mb-2">
                  <i className="fas fa-passport"></i> Passport: {client.passeport || 'N/A'}
                </p>
                <p className="text-muted mb-3">
                  <i className="fas fa-id-card"></i> ID: {client.id}
                </p>
                <div className="d-grid gap-2">
                  <Button
                    variant="outline-primary"
                    size="sm"
                    onClick={() => handleEdit(client)}
                  >
                    <i className="fas fa-edit"></i> Modifier
                  </Button>
                  <Button
                    variant="outline-danger"
                    size="sm"
                    onClick={() => handleDelete(client.id)}
                  >
                    <i className="fas fa-trash"></i> Supprimer
                  </Button>
                </div>
              </Card.Body>
            </Card>
          </Col>
        ))}
      </Row>

      <Modal show={showModal} onHide={() => setShowModal(false)}>
        <Modal.Header closeButton>
          <Modal.Title>{editingClient ? 'Modifier Client' : 'Nouveau Client'}</Modal.Title>
        </Modal.Header>
        <Form onSubmit={handleSubmit}>
          <Modal.Body>
            <Form.Group className="mb-3">
              <Form.Label>Nom *</Form.Label>
              <Form.Control
                type="text"
                value={formData.nom}
                onChange={(e) => setFormData({ ...formData, nom: e.target.value })}
                required
              />
            </Form.Group>
            <Form.Group className="mb-3">
              <Form.Label>Passport</Form.Label>
              <Form.Control
                type="text"
                value={formData.passeport}
                onChange={(e) => setFormData({ ...formData, passeport: e.target.value })}
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

export default Clients;

