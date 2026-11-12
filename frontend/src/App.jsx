
import { useEffect, useState } from 'react';
import api from './services/api';
import './App.css';

function App() {
    const [activeTab, setActiveTab] = useState('news');
    const [input, setInput] = useState('');
    const [result, setResult] = useState(null);
    const [history, setHistory] = useState([]);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState('');

    const tabs = [
        { id: 'news', label: 'Fake News', icon: '📰' },
        { id: 'spam', label: 'Spam SMS', icon: '📩' },
        { id: 'link', label: 'Fake Link', icon: '🔗' }
    ];

    const loadHistory = async () => {
        try {
            const data = await api.getHistory();
            setHistory(data);
        } catch (err) {
            setError(err.message);
        }
    };

    useEffect(() => {
        loadHistory();
    }, []);

    const handleVerify = async (e) => {
        e.preventDefault();

        if (!input.trim()) {
            setError('Please enter content to verify.');
            return;
        }

        setLoading(true);
        setError('');
        setResult(null);

        try {
            const data = await api.verify(activeTab, input.trim());
            setResult(data);
            setInput('');
            await loadHistory();
        } catch (err) {
            setError(err.message || 'Verification failed.');
        } finally {
            setLoading(false);
        }
    };

    const handleClearHistory = async () => {
        const confirmed = window.confirm(
            'Are you sure you want to clear all verification history?'
        );

        if (!confirmed) return;

        try {
            await api.clearHistory();
            setHistory([]);
        } catch (err) {
            setError(err.message);
        }
    };

    const getRiskClass = (level) => {
        return level?.toLowerCase() || 'low';
    };

    const getPageTitle = () => {
        if (activeTab === 'news') return 'Fake News Detection';
        if (activeTab === 'spam') return 'Spam SMS & Email Detection';
        if (activeTab === 'link') return 'Fake Link Detection';
        return 'Verification';
    };


const historyRecords = [];

for (let i = 0; i < history.length; i += 6) {

    const lines = history.slice(i, i + 6);

    if (lines.length < 6) continue;

    const getValue = (prefix) => {
        const line = lines.find(item => item.startsWith(prefix));
        return line ? line.substring(prefix.length).trim() : '';
    };

    historyRecords.push({
        id: i,
        input: getValue('Input: '),
        category: getValue('Category: '),
        score: Number(getValue('Risk Score: ')),
        suspicious: getValue('Suspicious: ') === 'true',
        status: getValue('Status: ')
    });
}

    return (
        <div className="app">

            {/* Sidebar */}

            <aside className="sidebar">

                <div className="brand">
                    <div className="brand-icon">N</div>
                    <div>
                        <h2>NEXUS</h2>
                        <span>VERIFY</span>
                    </div>
                </div>

                <p className="menu-heading">MAIN MENU</p>

                <button
                    className="nav-item active"
                    onClick={() => setActiveTab('news')}
                >
                    <span>🏠</span> Dashboard
                </button>

                <p className="menu-heading">VERIFICATION TOOLS</p>

                {tabs.map((tab) => (
                    <button
                        key={tab.id}
                        className={`nav-item ${
                            activeTab === tab.id ? 'selected' : ''
                        }`}
                        onClick={() => {
                            setActiveTab(tab.id);
                            setResult(null);
                            setError('');
                        }}
                    >
                        <span>{tab.icon}</span>
                        {tab.label}
                    </button>
                ))}

                <p className="menu-heading">MANAGEMENT</p>

                <button
                    className="nav-item"
                    onClick={loadHistory}
                >
                    <span>📋</span> Refresh History
                </button>

                <div className="sidebar-bottom">
                    <div className="security-card">
                        <span>🛡️</span>
                        <h4>Stay Protected</h4>
                        <p>Think before you trust. Verify before you share.</p>
                    </div>
                    <p className="sidebar-version">NEXUS VERIFY v1.0</p>
                </div>

            </aside>

            {/* Main Content */}

            <main className="main-content">

                <header className="topbar">
                    <div>
                        <p className="breadcrumb">Workspace / Dashboard</p>
                        <h2>Welcome to Nexus Verify</h2>
                    </div>

                    <div className="user-profile">
                        <div className="profile-avatar">NV</div>
                        <div>
                            <strong>Verification System</strong>
                            <span>Online</span>
                        </div>
                    </div>
                </header>

                <section className="page-content">

                    <div className="welcome-section">
                        <div>
                            <span className="eyebrow">
                                DIGITAL SAFETY PLATFORM
                            </span>
                            <h1>Verify Before You Trust.</h1>
                            <p>
                                Analyze suspicious news, messages and links
                                using intelligent rule-based verification.
                            </p>
                        </div>
                        <div className="welcome-shield">🛡️</div>
                    </div>

                    {/* Statistics */}

                    <div className="stats-grid">

                        <div className="stat-card">
                            <div className="stat-icon purple">🔍</div>
                            <div>
                                <span>Total Records</span>
                                <h3>{historyRecords.length}</h3>
                            </div>
                        </div>

                        <div className="stat-card">
                            <div className="stat-icon red">⚠️</div>
                            <div>
                                <span>Verification Tools</span>
                                <h3>03</h3>
                            </div>
                        </div>

                        <div className="stat-card">
                            <div className="stat-icon green">⚡</div>
                            <div>
                                <span>API Status</span>
                                <h3>Connected</h3>
                            </div>
                        </div>

                    </div>

                    {/* Verification Section */}

                    <div className="section-heading">
                        <div>
                            <span className="eyebrow">ANALYSIS CENTER</span>
                            <h2>{getPageTitle()}</h2>
                        </div>
                        <span className="live-badge">● SYSTEM ACTIVE</span>
                    </div>

                    <div className="verification-card">

                        <div className="verification-tabs">
                            {tabs.map((tab) => (
                                <button
                                    key={tab.id}
                                    className={
                                        activeTab === tab.id ? 'tab active-tab' : 'tab'
                                    }
                                    onClick={() => {
                                        setActiveTab(tab.id);
                                        setResult(null);
                                        setError('');
                                    }}
                                >
                                    {tab.icon} {tab.label}
                                </button>
                            ))}
                        </div>

                        <form onSubmit={handleVerify}>

                            <label className="input-label">
                                Enter content for verification
                            </label>

                            <textarea
                                value={input}
                                onChange={(e) => setInput(e.target.value)}
                                placeholder={
                                    activeTab === 'news'
                                        ? 'Paste news content or article text here...'
                                        : activeTab === 'spam'
                                        ? 'Paste suspicious SMS or email here...'
                                        : 'Paste a URL or message containing a link here...'
                                }
                                rows="6"
                            />

                            <div className="input-footer">
                                <span>Enter the complete content for better analysis.</span>

                                <button
                                    type="submit"
                                    className="verify-button"
                                    disabled={loading}
                                >
                                    {loading ? 'Analyzing...' : '🔍 Verify Now'}
                                </button>
                            </div>

                        </form>

                        {error && (
                            <div className="error-message">
                                ⚠️ {error}
                            </div>
                        )}

                    </div>

                    {/* Result Section */}

                    {result && (
                        <div className="result-card">

                            <div className="result-heading">
                                <div>
                                    <span className="eyebrow">
                                        VERIFICATION COMPLETE
                                    </span>
                                    <h2>Analysis Report</h2>
                                </div>

                                <span className={`risk-badge ${
                                    getRiskClass(result.riskLevel)
                                }`}>
                                    {result.riskLevel} RISK
                                </span>
                            </div>

                            <div className="result-category">
                                <span>Detected Category</span>
                                <h3>{result.category}</h3>
                            </div>

                            <div className="score-section">
                                <div className="score-info">
                                    <span>Risk Score</span>
                                    <strong>{result.riskScore}/100</strong>
                                </div>

                                <div className="score-track">
                                    <div
                                        className={`score-fill ${
                                            getRiskClass(result.riskLevel)
                                        }`}
                                        style={{
                                            width: `${result.riskScore}%`
                                        }}
                                    />
                                </div>
                            </div>

                            <div className="result-message">
                                <strong>
                                    {result.suspicious
                                        ? '⚠️ Suspicious Indicators Found'
                                        : '✓ No Major Suspicious Indicators'}
                                </strong>
                                <p>{result.message}</p>
                            </div>

                            <div className="recommendation">
                                <strong>💡 Recommendation</strong>
                                <p>{result.recommendation}</p>
                            </div>

                        </div>
                    )}

                    {/* History Section */}

                    <div className="section-heading history-heading">
                        <div>
                            <span className="eyebrow">RECENT ACTIVITY</span>
                            <h2>Verification History</h2>
                        </div>

                        <button
                            className="clear-button"
                            onClick={handleClearHistory}
                            disabled={history.length === 0}
                        >
                            Clear History
                        </button>
                    </div>

                    <div className="history-card">

                        {history.length === 0 ? (
                            <div className="empty-history">
                                <span>📋</span>
                                <p>No verification history available.</p>
                            </div>
                        ) : (
                            <div className="history-list">
                                
{historyRecords.length === 0 ? (
    <div className="empty-history">
        <span>📋</span>
        <p>No verification history available.</p>
    </div>
) : (
    <div className="history-list">
        {historyRecords.slice().reverse().map((record) => (
            <div className="history-record" key={record.id}>

                <div className="history-record-top">
                    <div>
                        <span className="history-category">
                            {record.category}
                        </span>
                        <h3>{record.input}</h3>
                    </div>

                    <span className={`risk-badge ${
                        record.score >= 60
                            ? 'high'
                            : record.score >= 30
                            ? 'medium'
                            : 'low'
                    }`}>
                        {record.score >= 60
                            ? 'HIGH RISK'
                            : record.score >= 30
                            ? 'MEDIUM RISK'
                            : 'LOW RISK'}
                    </span>
                </div>

                <div className="history-record-bottom">
                    <span>Risk Score: {record.score}/100</span>
                    <span>
                        {record.suspicious
                            ? '⚠️ Suspicious'
                            : '✓ No major indicators'}
                    </span>
                </div>

                <p className="history-status">{record.status}</p>

            </div>
        ))}
    </div>
)}

                            </div>
                        )}

                    </div>

                    <footer className="footer">
                        <p>
                            © 2026 NEXUS VERIFY. All rights reserved.
                        </p>
                        <p>VERIFY. ANALYZE. STAY SAFE.</p>
                    </footer>

                </section>

            </main>

        </div>
    );
}

export default App;
