import { useEffect, useMemo, useState } from 'react';
import { MapContainer, TileLayer, Marker, Popup, Circle } from 'react-leaflet';
import 'leaflet/dist/leaflet.css';

interface Unit { id: string; name: string; type: 'MILITARY'|'POLICE'|'SENSOR'; latitude: number; longitude: number; }
interface SpectrumActivity { id: string; band: 'VHF'|'UHF'|'SHF'; intensity: number; latitude: number; longitude: number; }
interface Incident { id: string; type: 'SIGNAL_JAMMING'|'UNIDENTIFIED_TRANSMISSION'|'INTERFERENCE'; severity: string; latitude: number; longitude: number; }

const BASIC_AUTH = 'hq:hqpass';
const API = 'http://127.0.0.1:8080/api';

export default function App() {
  const [units, setUnits] = useState<Unit[]>([]);
  const [spectrum, setSpectrum] = useState<SpectrumActivity[]>([]);
  const [incidents, setIncidents] = useState<Incident[]>([]);

  const headers = useMemo(() => ({
    Authorization: `Basic ${btoa(BASIC_AUTH)}`,
  }), []);

  useEffect(() => {
    const fetchAll = async () => {
      const [u, s, i] = await Promise.all([
        fetch(`${API}/units`, { headers }).then(r => r.json()),
        fetch(`${API}/spectrum`, { headers }).then(r => r.json()),
        fetch(`${API}/incidents`, { headers }).then(r => r.json()),
      ]);
      setUnits(u); setSpectrum(s); setIncidents(i);
    };
    fetchAll();
    const id = setInterval(fetchAll, 5000);
    return () => clearInterval(id);
  }, [headers]);

  const center = { lat: 38.95, lng: -76.95 };

  return (
    <div className="h-screen w-screen flex">
      <div className="w-64 bg-gray-900 text-white p-3">Filters</div>
      <div className="flex-1">
        <MapContainer center={center} zoom={11} style={{ height: '100%', width: '100%' }}>
          <TileLayer url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png" attribution="&copy; OpenStreetMap" />
          {units.map(u => (
            <Marker key={u.id} position={{ lat: u.latitude, lng: u.longitude }}>
              <Popup>{u.name} ({u.type})</Popup>
            </Marker>
          ))}
          {spectrum.map(s => (
            <Circle key={s.id} center={{ lat: s.latitude, lng: s.longitude }} radius={300 + s.intensity * 700} pathOptions={{ color: s.band === 'VHF' ? 'blue' : s.band === 'UHF' ? 'green' : 'purple', opacity: 0.5 }} />
          ))}
          {incidents.map(i => (
            <Marker key={i.id} position={{ lat: i.latitude, lng: i.longitude }}>
              <Popup>{i.type} - {i.severity}</Popup>
            </Marker>
          ))}
        </MapContainer>
      </div>
      <div className="w-80 bg-gray-100 p-3 overflow-y-auto">Event feed</div>
    </div>
  );
}
