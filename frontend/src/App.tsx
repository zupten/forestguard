import { useEffect, useRef, useState } from 'react'
import * as L from 'leaflet'

const BARX: L.LatLngTuple = [39.016944, -0.304722]
const INCENDIO: L.LatLngTuple = [39.041747, -0.293315]

// Encuadre general que incluye la península, Baleares y Canarias.
const ESPANA = L.latLngBounds([27.5, -18.5], [44.2, 4.5])
const ZONA_PRUEBA = L.latLngBounds(BARX, INCENDIO).pad(0.8)

export default function App() {
  type Incendio = {
    numeroParte: string
    provincia: string
    municipio: string
    fechaDeteccion: string
    latitud: number
    longitud: number
  }
  const contenedor = useRef<HTMLDivElement>(null)
  const mapa = useRef<L.Map | null>(null)
  const [estado, setEstado] = useState('Cargando incendios...')

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

    const cancelacion = new AbortController()
    const incendios = L.featureGroup().addTo(instancia)

    async function cargarIncendios() {
      try {
        const respuesta = await fetch('/api/incendios', {
          signal: cancelacion.signal,
        })

        if (!respuesta.ok) {
          throw new Error(`HTTP ${respuesta.status}`)
        }

        const datos: Incendio[] = await respuesta.json()
        if (cancelacion.signal.aborted) return

        for (const incendio of datos) {
          // Usar texto para no interpretar los datos como HTML.
          const contenido = document.createElement('div')
          contenido.style.whiteSpace = 'pre-line'
          contenido.textContent =
            `Incendio histórico EGIF\n` +
            `Parte: ${incendio.numeroParte}\n` +
            `Provincia (código): ${incendio.provincia}\n` +
            `Municipio (código): ${incendio.municipio}\n` +
            `Detección: ${incendio.fechaDeteccion.replace('T', ' ')}\n` +
            `Localización registrada; no representa el perímetro quemado.`

          L.circleMarker([incendio.latitud, incendio.longitud], {
            radius: 5,
            color: '#b91c1c',
            fillColor: '#f87171',
            fillOpacity: 0.8,
          })
            .bindPopup(contenido)
            .addTo(incendios)
        }

        setEstado(`${datos.length} incendios históricos cargados`)

        if (datos.length > 0) {
          instancia.fitBounds(incendios.getBounds(), {
            padding: [20, 20],
            maxZoom: 12,
          })
        }
      } catch (error) {
        if (cancelacion.signal.aborted) return

      console.error('Error al cargar los incendios:', error)
      setEstado('No se pudieron cargar los incendios. Comprueba la API.')
      }
    }

    void cargarIncendios()

    // Actualizar el mapa si cambia el tamaño de su contenedor.
    const observador = new ResizeObserver(() => {
      instancia.invalidateSize()
    })
    observador.observe(contenedor.current)

    // Liberar recursos al desmontar el componente.
    return () => {
      cancelacion.abort()
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
        <p role="status">{estado}</p>
        <p>
          Azul: estación BARX · Rojo: incendios históricos EGIF.
          No indica incendios activos ni predicciones.
        </p>
      </footer>
    </main>
  )
}