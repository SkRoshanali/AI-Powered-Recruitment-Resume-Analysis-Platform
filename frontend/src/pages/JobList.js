import React, { useState, useEffect } from 'react';
import { Container, Row, Col, Card, Badge, Form, Button } from 'react-bootstrap';
import { useNavigate } from 'react-router-dom';
import { getJobs, searchJobs } from '../services/api';

function JobList() {
  const [jobs, setJobs] = useState([]);
  const [searchKeyword, setSearchKeyword] = useState('');
  const navigate = useNavigate();

  useEffect(() => {
    loadJobs();
  }, []);

  const loadJobs = async () => {
    try {
      const response = await getJobs();
      setJobs(response.data.content);
    } catch (error) {
      console.error('Error loading jobs:', error);
    }
  };

  const handleSearch = async (e) => {
    e.preventDefault();
    try {
      const response = await searchJobs(searchKeyword);
      setJobs(response.data.content);
    } catch (error) {
      console.error('Error searching jobs:', error);
    }
  };

  return (
    <Container className="mt-4">
      <h2 className="mb-4">Available Jobs</h2>
      
      <Form onSubmit={handleSearch} className="mb-4">
        <Row>
          <Col md={10}>
            <Form.Control
              type="text"
              placeholder="Search jobs by keyword..."
              value={searchKeyword}
              onChange={(e) => setSearchKeyword(e.target.value)}
            />
          </Col>
          <Col md={2}>
            <Button type="submit" className="w-100">Search</Button>
          </Col>
        </Row>
      </Form>

      <Row>
        {jobs.map((job) => (
          <Col md={6} lg={4} key={job.id}>
            <Card className="job-card mb-3" onClick={() => navigate(`/jobs/${job.id}`)}>
              <Card.Body>
                <Card.Title>{job.title}</Card.Title>
                <Card.Subtitle className="mb-2 text-muted">{job.companyName}</Card.Subtitle>
                <Card.Text>
                  <small>{job.location} • {job.jobType}</small>
                </Card.Text>
                <div className="mb-2">
                  {job.requiredSkills?.slice(0, 5).map((skill, index) => (
                    <Badge key={index} bg="primary">{skill}</Badge>
                  ))}
                </div>
                <Card.Text className="text-muted">
                  <small>Experience: {job.minExperience}-{job.maxExperience} years</small>
                </Card.Text>
              </Card.Body>
            </Card>
          </Col>
        ))}
      </Row>
    </Container>
  );
}

export default JobList;
