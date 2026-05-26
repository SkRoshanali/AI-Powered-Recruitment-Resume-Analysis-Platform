import React, { useState, useEffect } from 'react';
import { Container, Card, Badge, Button, Form, Alert } from 'react-bootstrap';
import { useParams, useNavigate } from 'react-router-dom';
import { getJobById, applyForJob } from '../services/api';

function JobDetails() {
  const { id } = useParams();
  const navigate = useNavigate();
  const [job, setJob] = useState(null);
  const [coverLetter, setCoverLetter] = useState('');
  const [message, setMessage] = useState('');
  const [error, setError] = useState('');

  useEffect(() => {
    loadJob();
  }, [id]);

  const loadJob = async () => {
    try {
      const response = await getJobById(id);
      setJob(response.data);
    } catch (error) {
      console.error('Error loading job:', error);
    }
  };

  const handleApply = async () => {
    try {
      await applyForJob(id, coverLetter);
      setMessage('Application submitted successfully!');
      setError('');
      setTimeout(() => navigate('/dashboard'), 2000);
    } catch (err) {
      setError(err.response?.data?.message || 'Application failed');
      setMessage('');
    }
  };

  if (!job) return <Container className="mt-4">Loading...</Container>;

  return (
    <Container className="mt-4">
      <Card>
        <Card.Body>
          <Card.Title as="h2">{job.title}</Card.Title>
          <Card.Subtitle className="mb-3 text-muted h5">{job.companyName}</Card.Subtitle>
          
          <div className="mb-3">
            <strong>Location:</strong> {job.location}<br />
            <strong>Type:</strong> {job.jobType}<br />
            <strong>Experience Level:</strong> {job.experienceLevel}<br />
            <strong>Salary:</strong> {job.salaryRange || 'Not specified'}<br />
            <strong>Experience Required:</strong> {job.minExperience}-{job.maxExperience} years
          </div>

          <div className="mb-3">
            <strong>Required Skills:</strong><br />
            {job.requiredSkills?.map((skill, index) => (
              <Badge key={index} bg="primary" className="me-1">{skill}</Badge>
            ))}
          </div>

          <div className="mb-4">
            <strong>Description:</strong>
            <p className="mt-2">{job.description}</p>
          </div>

          {message && <Alert variant="success">{message}</Alert>}
          {error && <Alert variant="danger">{error}</Alert>}

          <Form.Group className="mb-3">
            <Form.Label><strong>Cover Letter (Optional)</strong></Form.Label>
            <Form.Control
              as="textarea"
              rows={5}
              value={coverLetter}
              onChange={(e) => setCoverLetter(e.target.value)}
              placeholder="Tell us why you're a great fit for this role..."
            />
          </Form.Group>

          <Button variant="primary" size="lg" onClick={handleApply}>
            Apply Now
          </Button>
        </Card.Body>
      </Card>
    </Container>
  );
}

export default JobDetails;
