import React, { useState, useEffect } from 'react';
import { Card, Row, Col, Button, Form, Modal, Alert } from 'react-bootstrap';
import axios from 'axios';

const API_URL = 'http://localhost:8080/api';

const Chambres = () => {
  const [chambres, setChambres] = useState([]);
  const [showModal, setShowModal] = useState(false);
  const [editingChambre, setEditingChambre] = useState(null);
  const [formData, setFormData] = useState({
    numero: '',
    type: '',
    prixNuit: '',
    disponible: true
  });
  const [message, setMessage] = useState({ type: '', text: '' });
  const [filterType, setFilterType] = useState('');
  const [filterDisponible, setFilterDisponible] = useState('');
  const [chambreReservations, setChambreReservations] = useState({});

  useEffect(() => {
    fetchChambres();
  }, []);

  const fetchChambres = async () => {
    try {
      let url = `${API_URL}/chambres`;
      const params = new URLSearchParams();
      if (filterType) params.append('type', filterType);
      if (filterDisponible) params.append('disponible', filterDisponible);
      if (params.toString()) url += '?' + params.toString();

      const response = await axios.get(url);
      setChambres(response.data);
      
      // Vérifier les réservations pour chaque chambre
      const reservationsMap = {};
      for (const chambre of response.data) {
        const hasReservations = await checkChambreReservations(chambre.id);
        reservationsMap[chambre.id] = hasReservations;
      }
      setChambreReservations(reservationsMap);
    } catch (error) {
      console.error('Error fetching chambres:', error);
    }
  };

  const checkChambreReservations = async (chambreId) => {
    try {
      const response = await axios.get(`${API_URL}/reservations`);
      const reservations = response.data.filter(reservation => 
        reservation.chambre && reservation.chambre.id === chambreId && 
        reservation.statut === 'Confirmée'
      );
      return reservations.length > 0;
    } catch (error) {
      console.error('Error checking reservations:', error);
      return false;
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const data = {
        ...formData,
        prixNuit: parseFloat(formData.prixNuit)
      };

      if (editingChambre) {
        const response = await axios.put(`${API_URL}/chambres/${editingChambre.id}`, data);
        setMessage({ type: 'success', text: 'Chambre mise à jour avec succès!' });
      } else {
        await axios.post(`${API_URL}/chambres`, data);
        setMessage({ type: 'success', text: 'Chambre créée avec succès!' });
      }

      setShowModal(false);
      fetchChambres();
      resetForm();
    } catch (error) {
      let errorMessage = 'Erreur lors de la sauvegarde';
      let errorType = 'danger';
      if (error.response && error.response.data) {
        try {
          const errorData = JSON.parse(error.response.data);
          errorMessage = errorData.error || errorMessage;
          errorType = errorData.type || errorType;
        } catch (parseError) {
          errorMessage = error.response.data || errorMessage;
        }
      }
      setMessage({ type: errorType, text: errorMessage });
    }
  };

  const handleEdit = (chambre) => {
    // Empêcher l'édition si la chambre a des réservations actives
    if (chambreReservations[chambre.id]) {
      setMessage({ 
        type: 'warning', 
        text: `🚫 La chambre ${chambre.numero} ne peut pas être modifiée car elle a des réservations confirmées. La disponibilité ne peut être changée qu'après annulation ou suppression de toutes les réservations.` 
      });
      return;
    }
    
    setEditingChambre(chambre);
    setFormData({
      numero: chambre.numero,
      type: chambre.type,
      prixNuit: chambre.prixNuit,
      disponible: chambre.disponible
    });
    setShowModal(true);
  };

  const handleDelete = async (id) => {
    if (window.confirm('Êtes-vous sûr de vouloir supprimer cette chambre?')) {
      try {
        await axios.delete(`${API_URL}/chambres/${id}`);
        setMessage({ type: 'success', text: 'Chambre supprimée avec succès!' });
        fetchChambres();
      } catch (error) {
        setMessage({ type: 'danger', text: 'Erreur: ' + error.message });
      }
    }
  };

  const resetForm = () => {
    setFormData({ numero: '', type: '', prixNuit: '', disponible: true });
    setEditingChambre(null);
  };

  return (
    <div>
      <div className="d-flex justify-content-between align-items-center mb-4">
        <h2>
          <i className="fas fa-door-open"></i> Gestion des Chambres
        </h2>
        <Button variant="primary" onClick={() => { resetForm(); setShowModal(true); }}>
          <i className="fas fa-plus"></i> Nouvelle Chambre
        </Button>
      </div>

      {message.text && (
        <Alert variant={message.type} dismissible onClose={() => setMessage({ type: '', text: '' })}>
          <Alert.Heading>
            {message.type === 'warning' ? '⚠️ Attention' : 
             message.type === 'danger' ? '❌ Erreur' : 
             message.type === 'success' ? '✅ Succès' : 'ℹ️ Information'}
          </Alert.Heading>
          {message.text}
        </Alert>
      )}

      <Card className="mb-4">
        <Card.Body>
          <Form>
            <Row className="g-3">
              <Col md={4}>
                <Form.Label>Type</Form.Label>
                <Form.Control
                  type="text"
                  placeholder="Ex: Single, Double"
                  value={filterType}
                  onChange={(e) => setFilterType(e.target.value)}
                />
              </Col>
              <Col md={4}>
                <Form.Label>Disponibilité</Form.Label>
                <Form.Select
                  value={filterDisponible}
                  onChange={(e) => setFilterDisponible(e.target.value)}
                >
                  <option value="">Tous</option>
                  <option value="true">Disponible</option>
                  <option value="false">Indisponible</option>
                </Form.Select>
              </Col>
              <Col md={4} className="d-flex align-items-end">
                <Button variant="secondary" onClick={fetchChambres} className="w-100">
                  <i className="fas fa-search"></i> Filtrer
                </Button>
              </Col>
            </Row>
          </Form>
        </Card.Body>
      </Card>

      <Row className="g-4">
        {chambres.map((chambre) => (
          <Col key={chambre.id} md={6} lg={4}>
            <Card className="h-100">
              <Card.Body>
                <div className="d-flex justify-content-between align-items-start mb-3">
                  <h5>Chambre {chambre.numero}</h5>
                  <span
                    className={`badge ${
                      chambre.disponible ? 'bg-success' : 'bg-danger'
                    }`}
                  >
                    {chambre.disponible ? 'Disponible' : 'Indisponible'}
                  </span>
                </div>
                <p className="text-muted mb-2">
                  <i className="fas fa-tag"></i> {chambre.type}
                </p>
                <p style={{ fontSize: '1.5rem', color: '#667eea', fontWeight: 'bold' }}>
                  {chambre.prixNuit} DH/nuit
                </p>
                <div className="d-grid gap-2">
                  <Button
                    variant={chambreReservations[chambre.id] ? "outline-secondary" : "outline-primary"}
                    size="sm"
                    onClick={() => handleEdit(chambre)}
                    disabled={chambreReservations[chambre.id]}
                    title={chambreReservations[chambre.id] ? "Impossible de modifier: chambre avec réservations confirmées" : ""}
                  >
                    <i className="fas fa-edit"></i> Modifier
                  </Button>
                  <Button
                    variant="outline-danger"
                    size="sm"
                    onClick={() => handleDelete(chambre.id)}
                  >
                    <i className="fas fa-trash"></i> Supprimer
                  </Button>
                </div>
                {chambreReservations[chambre.id] && (
                  <div className="alert alert-warning alert-sm mt-2 mb-0 p-2">
                    <i className="fas fa-exclamation-triangle text-warning me-1"></i>
                    <strong>Chambre réservée</strong><br/>
                    <small>Modification de disponibilité impossible</small>
                  </div>
                )}
              </Card.Body>
            </Card>
          </Col>
        ))}
      </Row>

      <Modal show={showModal} onHide={() => setShowModal(false)}>
        <Modal.Header closeButton>
          <Modal.Title>
            {editingChambre ? 'Modifier Chambre' : 'Nouvelle Chambre'}
          </Modal.Title>
        </Modal.Header>
        <Form onSubmit={handleSubmit}>
          <Modal.Body>
            <Form.Group className="mb-3">
              <Form.Label>Numéro *</Form.Label>
              <Form.Control
                type="text"
                value={formData.numero}
                onChange={(e) => setFormData({ ...formData, numero: e.target.value })}
                required
              />
            </Form.Group>
            <Form.Group className="mb-3">
              <Form.Label>Type *</Form.Label>
              <Form.Select
                value={formData.type}
                onChange={(e) => setFormData({ ...formData, type: e.target.value })}
                required
              >
                <option value="">Sélectionner un type</option>
                <option value="Single">Single</option>
                <option value="Double">Double</option>
                <option value="Suite">Suite</option>
                <option value="Deluxe">Deluxe</option>
              </Form.Select>
            </Form.Group>
            <Form.Group className="mb-3">
              <Form.Label>Prix par Nuit (DH) *</Form.Label>
              <Form.Control
                type="number"
                step="0.01"
                value={formData.prixNuit}
                onChange={(e) => setFormData({ ...formData, prixNuit: e.target.value })}
                required
              />
            </Form.Group>
            <Form.Check
              type="checkbox"
              label="Disponible"
              checked={formData.disponible}
              onChange={(e) => setFormData({ ...formData, disponible: e.target.checked })}
            />
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

export default Chambres;

