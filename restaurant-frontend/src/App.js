import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Layout from './components/common/Layout';
import AdminDashboard from './pages/Admin/AdminDashboard';
import RestaurantList from './pages/Customer/RestaurantList';
import RestaurantDetail from './pages/Customer/RestaurantDetail';
import RestaurantManagement from './pages/RestaurantOwner/RestaurantManagement';
import RestaurantCreateForm from './pages/Customer/RestaurantCreateForm';

function App() {
    return (
        <Router>
            <Layout>
                <Routes>
                    {/* Public Routes */}
                    <Route path="/" element={<RestaurantList />} />
                    <Route path="/restaurants/:id" element={<RestaurantDetail />} />

                    {/* Restaurant Creation - Now Public */}
                    <Route path="/restaurants/create" element={<RestaurantCreateForm />} />

                    {/* Other Public Routes */}
                    <Route path="/admin" element={<AdminDashboard />} />
                    <Route path="/manage/:id" element={<RestaurantManagement />} />

                    {/* 404 Not Found */}
                    <Route path="*" element={<div>404 Not Found</div>} />
                </Routes>
            </Layout>
        </Router>
    );
}

export default App;