import { useEffect, useRef } from 'react'
import * as L from 'leaflet'

const BARX: L.LatLngTuple = [39.016944, -0.304722]
const INCENDIO: L.LatLngTuple = [39.041747, -0.293315]

// Encuadre general que incluye la península, Baleares y Canarias.
const ESPANA = L.latLngBounds([27.5, -18.5], [44.2, 4.5])
const ZONA_PRUEBA = L.latLngBounds(BARX, INCENDIO).pad(0.8)

export default function App() {
  const contenedor = useRef<HTMLDivElement>(null)
  const mapa = useRef<L.Map | null>(null)

  useEffect(() => {
    if (!contenedor.current) return

    const instancia = L.map(contenedor.current)
    mapa.current = instancia

    L.tileLayer('https://tile.openstreetmap.org/{z}/{x}/{y}.png', {
      maxZoom: 19,
      attribution:
        '&copy; <a href="https://www.openstreetmap.org/copyright">' +
        'OpenStreetMap</a> contributors',
    }).addTo(instancia)

    instancia.fitBounds(ESPANA)

    L.circleMarker(BARX, {
      radius: 7,
      color: '#0369a1',
      fillColor: '#38bdf8',
      fillOpacity: 0.9,
    })
      .addTo(instancia)
      .bindPopup(
        '<strong>Estación AEMET BARX</strong><br>' +
        'Indicativo: 8072Y<br>Ubicación de referencia meteorológica.',
      )

    L.circleMarker(INCENDIO, {
      radius: 7,
      color: '#b91c1c',
      fillColor: '#f87171',
      fillOpacity: 0.9,
    })
      .addTo(instancia)
      .bindPopup(
        '<strong>Incendio histórico EGIF</strong><br>' +
        'Identificador: 2021460071<br>' +
        'Simat de la Valldigna · 25/07/2021<br>' +
        'Localización registrada; no representa el perímetro quemado.',
      )

    // Actualizar el mapa si cambia el tamaño de su contenedor.
    const observador = new ResizeObserver(() => {
      instancia.invalidateSize()
    })
    observador.observe(contenedor.current)

    // Liberar recursos al desmontar el componente.
    return () => {
      observador.disconnect()
      instancia.remove()
      mapa.current = null
    }
  }, [])

  return (
    <main className="flex h-full flex-col bg-slate-100 text-slate-900">
      <header className="border-b border-slate-200 bg-white px-5 py-4">
        <h1 className="text-2xl font-bold text-emerald-800">
          ForestGuard
        </h1>
        <p className="mt-1 text-sm text-slate-600">
          Exploración territorial · Prototipo del TFG
        </p>

        <nav aria-label="Encuadre del mapa" className="mt-3 flex gap-3">
          <button
            type="button"
            className="cursor-pointer rounded bg-slate-800 px-4 py-2 text-sm text-white hover:bg-slate-700"
            onClick={() => mapa.current?.fitBounds(ESPANA)}
          >
            Ver España
          </button>
          <button
            type="button"
            className="cursor-pointer rounded bg-emerald-700 px-4 py-2 text-sm text-white hover:bg-emerald-600"
            onClick={() =>
              mapa.current?.fitBounds(ZONA_PRUEBA, { maxZoom: 14 })
            }
          >
            Zona de prueba
          </button>
        </nav>
      </header>

      <section aria-label="Mapa de estudio" className="min-h-0 flex-1">
        <div ref={contenedor} />
      </section>

      <footer className="border-t border-slate-200 bg-white px-5 py-3 text-sm">
        Azul: estación BARX · Rojo: incendio histórico de Simat.
        Datos de muestra; no indica incendios activos ni predicciones.
      </footer>
    </main>
  )
}