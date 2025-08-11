# chatbot

# Nutrition Chatbot MCP Client 🥗🤖

A comprehensive nutrition-focused chatbot that serves as an expert advisor based on Dr. Cate Shanahan's "Deep Nutrition" principles. This intelligent system combines Retrieval-Augmented Generation (RAG) with MCP (Model Context Protocol) client capabilities to provide personalized nutrition guidance and meal tracking functionality.

## 🌟 Features

### Nutrition Expertise
- **Deep Nutrition Knowledge Base**: Built on Dr. Cate Shanahan's "Deep Nutrition" book summary through RAG implementation
- **Personalized Nutrition Advice**: Provides evidence-based nutritional guidance and recommendations
- **Harmful Food Detection**: Identifies and warns about harmful oils and processed foods
- **Meal Analysis**: Comprehensive nutritional breakdown of meals and food combinations

### Meal Tracking & Analytics
- **User Management**: Add and manage user profiles with personalized nutrition goals
- **Food Database**: Extensive food database with detailed nutritional information
- **Meal Logging**: Easy meal entry and tracking system
- **Calorie Monitoring**: Track daily calorie consumption and remaining allowances
- **Macronutrient Analysis**: Monitor carbohydrates, fats, and proteins intake
- **Food Frequency Tracking**: Track consumption patterns of specific foods
- **Harmful Oils Monitoring**: Specialized tracking for harmful oil consumption

### Technical Capabilities
- **MCP Client Integration**: Seamless connection to MCP server through Server-Sent Events (SSE)
- **Memory Advisor**: Maintains conversation context and user preferences
- **Real-time Data Sync**: Live updates between chatbot and database
- **Interactive Query System**: Natural language processing for nutrition queries

## 🛠️ Prerequisites

Before setting up the Nutrition Chatbot MCP Client, ensure you have the following installed:

- **Python 3.8+**
- **Node.js 16+** (if using JavaScript components)
- **pip** (Python package manager)
- **Virtual Environment** (recommended)
- **MCP Server** (must be running and accessible)
- **Database System** (PostgreSQL/MySQL/SQLite)

### Required Dependencies
```bash
# Core dependencies (example - adjust based on actual requirements)
anthropic>=0.3.0
fastapi>=0.104.0
uvicorn>=0.24.0
sqlalchemy>=2.0.0
pydantic>=2.0.0
python-multipart>=0.0.6
requests>=2.31.0
```

## 📦 Setup

### 1. Clone the Repository
```bash
git clone https://github.com/bahgaat/chatbot-mcp-client-.git
cd chatbot-mcp-client-
```

### 2. Create Virtual Environment
```bash
python -m venv nutrition_chatbot_env
source nutrition_chatbot_env/bin/activate  # On Windows: nutrition_chatbot_env\Scripts\activate
```

### 3. Install Dependencies
```bash
pip install -r requirements.txt
```

### 4. Environment Configuration
Create a `.env` file in the root directory:
```env
# MCP Server Configuration
MCP_SERVER_URL=http://localhost:8000
MCP_SERVER_SSE_ENDPOINT=http://localhost:8000/events

# Database Configuration
DATABASE_URL=sqlite:///nutrition_tracker.db

# API Keys
ANTHROPIC_API_KEY=your_anthropic_api_key_here

# Application Settings
DEBUG=True
LOG_LEVEL=INFO
```

### 5. Database Setup
```bash
# Initialize database tables
python manage.py migrate
# Or using SQLAlchemy
python init_db.py
```

### 6. Start the Application
```bash
# Start the chatbot client
python main.py

# Or using uvicorn for FastAPI
uvicorn main:app --reload --port 8001
```

## 🔧 How It Works

### Architecture Overview
```
┌─────────────────┐    SSE     ┌─────────────────┐
│   Chatbot MCP   │◄────────►  │   MCP Server    │
│     Client      │            │                 │
└─────────────────┘            └─────────────────┘
         │                              │
         │                              │
         ▼                              ▼
┌─────────────────┐            ┌─────────────────┐
│  Deep Nutrition │            │   Food Database │
│   RAG System    │            │   (Users, Foods,│
│                 │            │     Meals)      │
└─────────────────┘            └─────────────────┘
```

### Core Workflow

1. **User Interaction**: Users communicate with the chatbot using natural language queries
2. **Intent Recognition**: The system identifies whether the query is about:
   - Nutritional advice (routed to RAG system)
   - Data operations (routed to MCP server)
   - Analytics requests (processed locally with MCP data)

3. **RAG Processing**: For nutrition questions:
   - Queries the Deep Nutrition knowledge base
   - Retrieves relevant Dr. Cate Shanahan principles
   - Generates contextually appropriate responses

4. **MCP Operations**: For data operations:
   - Connects to MCP server via SSE
   - Performs CRUD operations on nutrition database
   - Returns real-time updates to the chatbot

5. **Response Generation**: Combines retrieved information with conversational AI to provide comprehensive, personalized responses

### Key Components

#### Memory Advisor
- Maintains user conversation history
- Tracks personal preferences and dietary restrictions
- Provides context-aware recommendations
- Remembers previous meal logging sessions

#### RAG System (Deep Nutrition)
- Vector database of Dr. Cate Shanahan's nutritional principles
- Semantic search capabilities for relevant content retrieval
- Evidence-based nutritional guidance
- Focus on whole foods and traditional dietary wisdom

#### MCP Client Integration
- Real-time communication with MCP server
- Handles user, food, and meal database operations
- Provides seamless data synchronization
- Supports concurrent user sessions

## ⚙️ MCP Client Configuration

### Connection Setup
The MCP client establishes connection through Server-Sent Events (SSE) for real-time bidirectional communication:

```python
# Example MCP client configuration
import asyncio
import aiohttp
from typing import AsyncGenerator

class MCPClient:
    def __init__(self, server_url: str, sse_endpoint: str):
        self.server_url = server_url
        self.sse_endpoint = sse_endpoint
        self.session = None
    
    async def connect(self):
        """Establish SSE connection to MCP server"""
        self.session = aiohttp.ClientSession()
        async with self.session.get(self.sse_endpoint) as response:
            async for line in response.content:
                if line:
                    yield self.process_sse_message(line.decode())
    
    async def send_command(self, command: dict):
        """Send command to MCP server"""
        async with self.session.post(
            f"{self.server_url}/api/command", 
            json=command
        ) as response:
            return await response.json()
```

### Available MCP Operations

#### User Management
```python
# Add new user
await mcp_client.send_command({
    "action": "add_user",
    "data": {
        "name": "John Doe",
        "age": 30,
        "weight": 70,
        "height": 175,
        "activity_level": "moderate"
    }
})
```

#### Food Database Operations
```python
# Add new food item
await mcp_client.send_command({
    "action": "add_food",
    "data": {
        "name": "Organic Spinach",
        "calories_per_100g": 23,
        "carbs": 3.6,
        "protein": 2.9,
        "fat": 0.4,
        "category": "vegetables"
    }
})
```

#### Meal Tracking
```python
# Log meal
await mcp_client.send_command({
    "action": "add_meal",
    "data": {
        "user_id": 1,
        "foods": [
            {"food_id": 1, "quantity": 100},
            {"food_id": 2, "quantity": 50}
        ],
        "meal_type": "lunch",
        "timestamp": "2024-01-15T12:30:00Z"
    }
})
```

### SSE Event Handling
The client processes various SSE events from the MCP server:

```python
def process_sse_message(self, message: str):
    """Process incoming SSE messages from MCP server"""
    try:
        event_data = json.loads(message)
        event_type = event_data.get('type')
        
        if event_type == 'user_added':
            self.handle_user_added(event_data['data'])
        elif event_type == 'meal_logged':
            self.handle_meal_logged(event_data['data'])
        elif event_type == 'nutrition_update':
            self.handle_nutrition_update(event_data['data'])
            
    except json.JSONDecodeError:
        logger.error(f"Invalid SSE message format: {message}")
```

## 📊 Usage Examples

### Nutrition Consultation
```
User: "What does Dr. Cate say about seed oils?"
Bot: "Based on Dr. Cate Shanahan's Deep Nutrition principles, seed oils like soybean, 
      canola, and corn oil are considered harmful due to their high omega-6 content 
      and processing methods. She recommends traditional fats like olive oil, 
      coconut oil, and animal fats instead..."
```

### Meal Tracking
```
User: "I just ate a grilled chicken breast with broccoli and olive oil"
Bot: "Great choice! I've logged your meal. Here's the breakdown:
      - Calories: 320
      - Protein: 45g
      - Carbs: 8g  
      - Fat: 12g
      You have 1,680 calories remaining for today."
```

### Analytics Queries
```
User: "How many times did I eat harmful oils this week?"
Bot: "This week, you consumed harmful oils 3 times:
      - Monday: French fries (likely cooked in vegetable oil)
      - Wednesday: Packaged cookies (containing soybean oil)  
      - Friday: Restaurant salad dressing (canola oil)
      
      Consider replacing these with whole food alternatives!"
```

## 🔍 Advanced Features

### Personalized Recommendations
- Analyzes eating patterns to suggest improvements
- Identifies nutritional deficiencies based on logged meals
- Recommends meal timing based on Dr. Cate's principles
- Suggests food combinations for optimal nutrient absorption

### Progress Tracking
- Weekly and monthly nutrition summaries
- Visual progress charts for macronutrient balance
- Harmful food consumption trends
- Achievement badges for healthy eating milestones

### Integration Capabilities
- Export nutrition data to popular health apps
- Import meal data from fitness trackers
- API endpoints for third-party integrations
- Webhook support for real-time notifications

## 🛡️ Error Handling

The system includes robust error handling for:
- MCP server connection failures
- Database connectivity issues
- Invalid user input processing
- RAG system unavailability
- Rate limiting and API quotas

## 🧪 Testing

```bash
# Run unit tests
python -m pytest tests/

# Run integration tests with MCP server
python -m pytest tests/integration/

# Test MCP client connection
python test_mcp_connection.py
```

## 📈 Performance Considerations

- **Caching**: Implements Redis caching for frequent RAG queries
- **Connection Pooling**: Efficient database connection management
- **Async Processing**: Non-blocking operations for better responsiveness
- **Rate Limiting**: Protects against API abuse and ensures fair usage

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🙏 Acknowledgments

- **Dr. Cate Shanahan** for her groundbreaking work in "Deep Nutrition"
- **MCP Protocol** for enabling seamless client-server communication
- **Anthropic** for providing the AI capabilities that power this chatbot
- **Open Source Community** for the various libraries and tools used in this project

## 📞 Support

For support, questions, or feature requests:
- 📧 Email: [your-email@example.com]
- 🐛 Issues: [GitHub Issues](https://github.com/bahgaat/chatbot-mcp-client-/issues)
- 💬 Discussions: [GitHub Discussions](https://github.com/bahgaat/chatbot-mcp-client-/discussions)

---

**Made with ❤️ for better nutrition and healthier living**
