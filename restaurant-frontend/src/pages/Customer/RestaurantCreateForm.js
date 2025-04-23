import React, { useState } from 'react';

const RestaurantCreateForm = () => {
    const [formData, setFormData] = useState({
        name: '',
        address: '',
        contactNumber: '',
        cuisineType: '',
        openingTime: '09:00',
        closingTime: '21:00',
        email: '',
        restaurantPassword: '',
        description: ''
    });
    const [coverImage, setCoverImage] = useState(null);
    const [isSubmitting, setIsSubmitting] = useState(false);
    const [error, setError] = useState('');
    const [success, setSuccess] = useState(false);

    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData(prev => ({
            ...prev,
            [name]: value
        }));
    };

    const handleImageChange = (e) => {
        setCoverImage(e.target.files[0]);
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setIsSubmitting(true);
        setError('');
        setSuccess(false);

        try {
            // Create FormData for multipart request
            const formDataToSend = new FormData();
            formDataToSend.append('restaurant', JSON.stringify(formData));
            if (coverImage) {
                formDataToSend.append('coverImage', coverImage);
            }

            // Make API call using fetch
            const response = await fetch('http://localhost:8081/restaurants/create', {
                method: 'POST',
                body: formDataToSend,
                // Don't set Content-Type header manually for FormData
                // The browser will set it automatically with the correct boundary
            });

            if (!response.ok) {
                const errorData = await response.json();
                throw new Error(errorData.message || 'Failed to create restaurant');
            }

            const responseData = await response.json();
            setSuccess(true);
            console.log('Restaurant created:', responseData);

            // Reset form after successful submission
            setFormData({
                name: '',
                address: '',
                contactNumber: '',
                cuisineType: '',
                openingTime: '09:00',
                closingTime: '21:00',
                email: '',
                restaurantPassword: '',
                description: ''
            });
            setCoverImage(null);
        } catch (err) {
            setError(err.message || 'Failed to create restaurant');
            console.error('Error creating restaurant:', err);
        } finally {
            setIsSubmitting(false);
        }
    };

    return (
        <div className="max-w-2xl mx-auto p-6 bg-white rounded-lg shadow-md">
            <h2 className="text-2xl font-bold mb-6">Create New Restaurant</h2>

            {error && (
                <div className="mb-4 p-4 bg-red-100 text-red-700 rounded">
                    {error}
                </div>
            )}

            {success && (
                <div className="mb-4 p-4 bg-green-100 text-green-700 rounded">
                    Restaurant account created successfully!
                    Admin needs to approve your restaurant
                </div>
            )}

            <form onSubmit={handleSubmit}>
                <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
                    {/* Left Column */}
                    <div>
                        <div className="mb-4">
                            <label className="block text-gray-700 mb-2">Restaurant Name*</label>
                            <input
                                type="text"
                                name="name"
                                value={formData.name}
                                onChange={handleChange}
                                className="w-full p-2 border rounded"
                                required
                            />
                        </div>

                        <div className="mb-4">
                            <label className="block text-gray-700 mb-2">Address*</label>
                            <input
                                type="text"
                                name="address"
                                value={formData.address}
                                onChange={handleChange}
                                className="w-full p-2 border rounded"
                                required
                            />
                        </div>

                        <div className="mb-4">
                            <label className="block text-gray-700 mb-2">Contact Number*</label>
                            <input
                                type="tel"
                                name="contactNumber"
                                value={formData.contactNumber}
                                onChange={handleChange}
                                className="w-full p-2 border rounded"
                                required
                            />
                        </div>

                        <div className="mb-4">
                            <label className="block text-gray-700 mb-2">Cuisine Type*</label>
                            <select
                                name="cuisineType"
                                value={formData.cuisineType}
                                onChange={handleChange}
                                className="w-full p-2 border rounded"
                                required
                            >
                                <option value="">Select cuisine</option>
                                <option value="Italian">Italian</option>
                                <option value="Chinese">Chinese</option>
                                <option value="Indian">Indian</option>
                                <option value="Mexican">Mexican</option>
                                <option value="American">American</option>
                                <option value="Japanese">Japanese</option>
                                <option value="Other">Other</option>
                            </select>
                        </div>
                    </div>

                    {/* Right Column */}
                    <div>
                        <div className="mb-4">
                            <label className="block text-gray-700 mb-2">Opening Time*</label>
                            <input
                                type="time"
                                name="openingTime"
                                value={formData.openingTime}
                                onChange={handleChange}
                                className="w-full p-2 border rounded"
                                required
                            />
                        </div>

                        <div className="mb-4">
                            <label className="block text-gray-700 mb-2">Closing Time*</label>
                            <input
                                type="time"
                                name="closingTime"
                                value={formData.closingTime}
                                onChange={handleChange}
                                className="w-full p-2 border rounded"
                                required
                            />
                        </div>

                        <div className="mb-4">
                            <label className="block text-gray-700 mb-2">Email*</label>
                            <input
                                type="email"
                                name="email"
                                value={formData.email}
                                onChange={handleChange}
                                className="w-full p-2 border rounded"
                                required
                            />
                        </div>

                        <div className="mb-4">
                            <label className="block text-gray-700 mb-2">Password*</label>
                            <input
                                type="password"
                                name="restaurantPassword"
                                value={formData.restaurantPassword}
                                onChange={handleChange}
                                className="w-full p-2 border rounded"
                                required
                            />
                        </div>
                    </div>
                </div>

                {/* Full Width Fields */}
                <div className="mb-4">
                    <label className="block text-gray-700 mb-2">Description</label>
                    <textarea
                        name="description"
                        value={formData.description}
                        onChange={handleChange}
                        className="w-full p-2 border rounded"
                        rows="3"
                    />
                </div>

                <div className="mb-6">
                    <label className="block text-gray-700 mb-2">Cover Image</label>
                    <input
                        type="file"
                        accept="image/*"
                        onChange={handleImageChange}
                        className="w-full p-2 border rounded"
                    />
                    {coverImage && (
                        <div className="mt-2">
                            <img
                                src={URL.createObjectURL(coverImage)}
                                alt="Preview"
                                className="h-32 object-cover rounded"
                            />
                        </div>
                    )}
                </div>

                <button
                    type="submit"
                    disabled={isSubmitting}
                    className={`w-full py-2 px-4 rounded text-white ${isSubmitting ? 'bg-blue-400' : 'bg-blue-600 hover:bg-blue-700'}`}
                >
                    {isSubmitting ? 'Creating...' : 'Create Restaurant'}
                </button>
            </form>
        </div>
    );
};

export default RestaurantCreateForm;