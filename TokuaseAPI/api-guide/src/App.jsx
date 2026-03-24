import { useEffect, useMemo, useState } from 'react'
import { API_ENDPOINTS } from './apiCatalog'
import './App.css'

function App() {
  const [baseUrl, setBaseUrl] = useState('http://localhost:8080')
  const [activeTag, setActiveTag] = useState('All')
  const [selectedId, setSelectedId] = useState(API_ENDPOINTS[0].id)
  const [pathValues, setPathValues] = useState({})
  const [queryValues, setQueryValues] = useState({})
  const [bodyText, setBodyText] = useState('')
  const [loading, setLoading] = useState(false)
  const [statusCode, setStatusCode] = useState('Idle')
  const [responseText, setResponseText] = useState('Run a request to see response output.')
  const [requestUrl, setRequestUrl] = useState('')

  const tags = useMemo(
    () => ['All', ...new Set(API_ENDPOINTS.map((item) => item.tag))],
    [],
  )

  const filteredEndpoints = useMemo(() => {
    if (activeTag === 'All') return API_ENDPOINTS
    return API_ENDPOINTS.filter((item) => item.tag === activeTag)
  }, [activeTag])

  const selectedEndpoint = useMemo(
    () => API_ENDPOINTS.find((item) => item.id === selectedId) ?? API_ENDPOINTS[0],
    [selectedId],
  )

  useEffect(() => {
    if (!filteredEndpoints.some((item) => item.id === selectedId)) {
      setSelectedId(filteredEndpoints[0].id)
    }
  }, [filteredEndpoints, selectedId])

  useEffect(() => {
    const nextPath = {}
    selectedEndpoint.pathParams.forEach((param) => {
      nextPath[param.name] = param.example
    })
    setPathValues(nextPath)

    const nextQuery = {}
    selectedEndpoint.queryParams.forEach((param) => {
      nextQuery[param.name] = param.example
    })
    setQueryValues(nextQuery)

    setBodyText(
      selectedEndpoint.sampleBody
        ? JSON.stringify(selectedEndpoint.sampleBody, null, 2)
        : '',
    )
    setStatusCode('Idle')
    setResponseText('Run a request to see response output.')
  }, [selectedEndpoint])

  const computedUrl = useMemo(() => {
    let path = selectedEndpoint.path
    selectedEndpoint.pathParams.forEach((param) => {
      const value = pathValues[param.name] || ''
      path = path.replace(`{${param.name}}`, encodeURIComponent(value))
    })

    const searchParams = new URLSearchParams()
    selectedEndpoint.queryParams.forEach((param) => {
      const value = queryValues[param.name]
      if (value !== '' && value !== undefined && value !== null) {
        searchParams.append(param.name, value)
      }
    })

    const normalized = baseUrl.replace(/\/$/, '')
    const queryString = searchParams.toString()
    const base = normalized + path
    return queryString ? base + '?' + queryString : base
  }, [baseUrl, selectedEndpoint, pathValues, queryValues])

  useEffect(() => {
    setRequestUrl(computedUrl)
  }, [computedUrl])

  const curlSnippet = useMemo(() => {
    const lines = [`curl -X ${selectedEndpoint.method} "${requestUrl}"`]
    if (selectedEndpoint.hasBody) {
      lines.push('  -H "Content-Type: application/json"')
      if (bodyText.trim()) {
        const escapedBody = bodyText.replaceAll("'", String.raw`'\''`)
        lines.push(`  -d '${escapedBody}'`)
      }
    }
    return lines.join(' \\\n')
  }, [selectedEndpoint, requestUrl, bodyText])

  const runRequest = async () => {
    setLoading(true)
    setStatusCode('Running')
    setResponseText('Loading...')

    try {
      const options = { method: selectedEndpoint.method, headers: {} }
      if (selectedEndpoint.hasBody) {
        options.headers['Content-Type'] = 'application/json'
        options.body = bodyText.trim() ? bodyText : '{}'
      }

      const response = await fetch(requestUrl, options)
      const raw = await response.text()
      let pretty = raw

      try {
        if (raw) {
          pretty = JSON.stringify(JSON.parse(raw), null, 2)
        }
      } catch {
        // Keep plain text when payload is not JSON.
      }

      setStatusCode(`${response.status} ${response.statusText}`)
      setResponseText(pretty || '(No response body)')
    } catch (error) {
      const message = error instanceof Error ? error.message : 'Unknown error'
      setStatusCode('Network error')
      setResponseText(
        `Request failed: ${message}.\n\nTip: confirm the Spring API is running and CORS allows this origin.`,
      )
    } finally {
      setLoading(false)
    }
  }

  const copyCurl = async () => {
    try {
      await navigator.clipboard.writeText(curlSnippet)
      setStatusCode('Copied')
    } catch {
      setStatusCode('Clipboard blocked')
    }
  }

  return (
    <div className="page-shell">
      <header className="hero">
        <p className="eyebrow">TokuBase API Companion</p>
        <h1>Learn every backend endpoint with live requests</h1>
        <p className="hero-copy">
          This frontend mirrors your Spring controllers and gives practical examples
          for each route. Edit params, fire requests, and inspect response payloads.
        </p>

        <div className="hero-actions">
          <label>
            <span>API Base URL</span>
            <input
              value={baseUrl}
              onChange={(event) => setBaseUrl(event.target.value)}
              placeholder="http://localhost:8080"
            />
          </label>

          <div className="link-row">
            <a href={`${baseUrl.replace(/\/$/, '')}/swagger-ui.html`} target="_blank" rel="noreferrer">
              Open Swagger UI
            </a>
            <a href={`${baseUrl.replace(/\/$/, '')}/api-docs`} target="_blank" rel="noreferrer">
              Open OpenAPI JSON
            </a>
          </div>
        </div>
      </header>

      <main className="layout">
        <section className="catalog panel">
          <div className="panel-header">
            <h2>Endpoint Catalog</h2>
            <div className="chip-row" role="tablist" aria-label="Filter endpoint groups">
              {tags.map((tag) => (
                <button
                  key={tag}
                  className={`chip ${activeTag === tag ? 'active' : ''}`}
                  onClick={() => setActiveTag(tag)}
                >
                  {tag}
                </button>
              ))}
            </div>
          </div>

          <div className="endpoint-list">
            {filteredEndpoints.map((endpoint) => (
              <button
                key={endpoint.id}
                className={`endpoint-card ${selectedId === endpoint.id ? 'selected' : ''}`}
                onClick={() => setSelectedId(endpoint.id)}
              >
                <span className={`method ${endpoint.method.toLowerCase()}`}>{endpoint.method}</span>
                <span className="path">{endpoint.path}</span>
                <span className="description">{endpoint.description}</span>
              </button>
            ))}
          </div>
        </section>

        <section className="tester panel">
          <div className="panel-header">
            <h2>Request Builder</h2>
            <span className="status">{statusCode}</span>
          </div>

          <article className="request-meta">
            <h3>{selectedEndpoint.title}</h3>
            <p>{selectedEndpoint.notes}</p>
          </article>

          <div className="input-grid">
            {selectedEndpoint.pathParams.map((param) => (
              <label key={param.name}>
                <span>Path: {param.name}</span>
                <input
                  value={pathValues[param.name] ?? ''}
                  onChange={(event) =>
                    setPathValues((prev) => ({ ...prev, [param.name]: event.target.value }))
                  }
                  placeholder={param.placeholder}
                />
              </label>
            ))}

            {selectedEndpoint.queryParams.map((param) => (
              <label key={param.name}>
                <span>Query: {param.name}</span>
                <input
                  value={queryValues[param.name] ?? ''}
                  onChange={(event) =>
                    setQueryValues((prev) => ({ ...prev, [param.name]: event.target.value }))
                  }
                  placeholder={param.placeholder}
                />
              </label>
            ))}
          </div>

          {selectedEndpoint.hasBody && (
            <label className="body-editor">
              <span>JSON Body</span>
              <textarea
                value={bodyText}
                onChange={(event) => setBodyText(event.target.value)}
                rows={10}
              />
            </label>
          )}

          <label>
            <span>Final Request URL</span>
            <input value={requestUrl} onChange={(event) => setRequestUrl(event.target.value)} />
          </label>

          <div className="button-row">
            <button className="primary" onClick={runRequest} disabled={loading}>
              {loading ? 'Running...' : 'Send Request'}
            </button>
            <button className="secondary" onClick={copyCurl}>Copy curl</button>
          </div>

          <div className="output-grid">
            <div>
              <h4>curl Preview</h4>
              <pre>{curlSnippet}</pre>
            </div>
            <div>
              <h4>Response</h4>
              <pre>{responseText}</pre>
            </div>
          </div>
        </section>
      </main>
    </div>
  )
}

export default App
