import { useEffect, useState } from "react";
import axios from "axios";

const API = "http://localhost:8080/api";

function App() {
  const [events, setEvents] = useState([]);
  const [message, setMessage] = useState("");

  const [eventForm, setEventForm] = useState({
    name: "",
    eventDate: "",
    eventTime: "",
    capacity: 50,
  });

  const [studentForm, setStudentForm] = useState({
    name: "",
    email: "",
    department: "",
  });

  const [registrationForm, setRegistrationForm] = useState({
    studentId: "",
    eventId: "",
  });

  const [checkInForm, setCheckInForm] = useState({
    studentId: "",
    eventId: "",
  });

  const loadEvents = async () => {
    const res = await axios.get(`${API}/events`);
    setEvents(res.data);
  };

  useEffect(() => {
    loadEvents().catch(() => setMessage("Backend is not running."));
  }, []);

  const createEvent = async (e) => {
    e.preventDefault();
    try {
      await axios.post(`${API}/events`, {
        ...eventForm,
        capacity: Number(eventForm.capacity),
      });
      setMessage("Event created successfully.");
      setEventForm({ name: "", eventDate: "", eventTime: "", capacity: 50 });
      loadEvents();
    } catch (err) {
      setMessage(err.response?.data?.message || "Could not create event.");
    }
  };

  const createStudent = async (e) => {
    e.preventDefault();
    try {
      const res = await axios.post(`${API}/students`, studentForm);
      setMessage(`Student created. Student ID: ${res.data.id}`);
      setStudentForm({ name: "", email: "", department: "" });
    } catch (err) {
      setMessage(err.response?.data?.message || "Could not create student.");
    }
  };

  const register = async (e) => {
    e.preventDefault();
    try {
      await axios.post(`${API}/registrations`, {
        studentId: Number(registrationForm.studentId),
        eventId: Number(registrationForm.eventId),
      });
      setMessage("Registration successful.");
      loadEvents();
    } catch (err) {
      setMessage(err.response?.data?.message || "Registration failed.");
    }
  };

  const checkIn = async (e) => {
    e.preventDefault();
    try {
      await axios.put(`${API}/registrations/check-in`, {
        studentId: Number(checkInForm.studentId),
        eventId: Number(checkInForm.eventId),
      });
      setMessage("Check-in successful.");
      loadEvents();
    } catch (err) {
      setMessage(err.response?.data?.message || "Check-in failed.");
    }
  };

  return (
    <div className="page">
      <header>
        <h1>College Event Management System</h1>
        <p>React + Spring Boot + SQLite</p>
      </header>

      {message && <div className="message">{message}</div>}

      <div className="grid">
        <section className="card">
          <h2>1. Create Event</h2>
          <form onSubmit={createEvent}>
            <input
              placeholder="Event name"
              value={eventForm.name}
              onChange={(e) => setEventForm({ ...eventForm, name: e.target.value })}
              required
            />
            <input
              type="date"
              value={eventForm.eventDate}
              onChange={(e) => setEventForm({ ...eventForm, eventDate: e.target.value })}
              required
            />
            <input
              type="time"
              value={eventForm.eventTime}
              onChange={(e) => setEventForm({ ...eventForm, eventTime: e.target.value })}
              required
            />
            <input
              type="number"
              min="1"
              placeholder="Maximum capacity"
              value={eventForm.capacity}
              onChange={(e) => setEventForm({ ...eventForm, capacity: e.target.value })}
              required
            />
            <button>Create Event</button>
          </form>
        </section>

        <section className="card">
          <h2>Create Student</h2>
          <form onSubmit={createStudent}>
            <input
              placeholder="Student name"
              value={studentForm.name}
              onChange={(e) => setStudentForm({ ...studentForm, name: e.target.value })}
              required
            />
            <input
              type="email"
              placeholder="Student email"
              value={studentForm.email}
              onChange={(e) => setStudentForm({ ...studentForm, email: e.target.value })}
              required
            />
            <input
              placeholder="Department"
              value={studentForm.department}
              onChange={(e) => setStudentForm({ ...studentForm, department: e.target.value })}
              required
            />
            <button>Create Student</button>
          </form>
        </section>

        <section className="card">
          <h2>2. Register for Event</h2>
          <form onSubmit={register}>
            <input
              type="number"
              min="1"
              placeholder="Student ID"
              value={registrationForm.studentId}
              onChange={(e) => setRegistrationForm({ ...registrationForm, studentId: e.target.value })}
              required
            />
            <input
              type="number"
              min="1"
              placeholder="Event ID"
              value={registrationForm.eventId}
              onChange={(e) => setRegistrationForm({ ...registrationForm, eventId: e.target.value })}
              required
            />
            <button>Register</button>
          </form>
          <small>Duplicate registrations and registrations after capacity are blocked.</small>
        </section>

        <section className="card">
          <h2>3. Check In</h2>
          <form onSubmit={checkIn}>
            <input
              type="number"
              min="1"
              placeholder="Student ID"
              value={checkInForm.studentId}
              onChange={(e) => setCheckInForm({ ...checkInForm, studentId: e.target.value })}
              required
            />
            <input
              type="number"
              min="1"
              placeholder="Event ID"
              value={checkInForm.eventId}
              onChange={(e) => setCheckInForm({ ...checkInForm, eventId: e.target.value })}
              required
            />
            <button>Check In</button>
          </form>
          <small>Only registered students can check in, and only on the event date.</small>
        </section>
      </div>

      <section className="card events">
        <div className="events-head">
          <h2>Events</h2>
          <button className="secondary" onClick={loadEvents}>Refresh</button>
        </div>

        {events.length === 0 ? (
          <p>No events yet.</p>
        ) : (
          <div className="table-wrap">
            <table>
              <thead>
                <tr>
                  <th>ID</th>
                  <th>Event</th>
                  <th>Date</th>
                  <th>Time</th>
                  <th>Capacity</th>
                  <th>Status</th>
                  <th>Summary</th>
                </tr>
              </thead>
              <tbody>
                {events.map((event) => (
                  <EventRow key={event.id} event={event} />
                ))}
              </tbody>
            </table>
          </div>
        )}
      </section>
    </div>
  );
}

function EventRow({ event }) {
  const [summary, setSummary] = useState(null);

  useEffect(() => {
    axios
      .get(`${API}/registrations/summary/${event.id}`)
      .then((res) => setSummary(res.data))
      .catch(() => {});
  }, [event.id]);

  return (
    <tr>
      <td>{event.id}</td>
      <td>{event.name}</td>
      <td>{event.eventDate}</td>
      <td>{event.eventTime}</td>
      <td>{event.capacity}</td>
      <td><span className={`status ${event.status.toLowerCase()}`}>{event.status}</span></td>
      <td>
        {summary
          ? `${summary.registeredCount} registered / ${summary.checkedInCount} checked in`
          : "Loading..."}
      </td>
    </tr>
  );
}

export default App;
