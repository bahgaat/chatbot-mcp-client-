# Nutrition Expert Chatbot (MCP Client)

This project is a **Nutrition Expert Chatbot** that provides personalized nutrition insights and answers questions based on *Deep Nutrition* by Dr. Cate Shanahan.  
It integrates a **Retrieval-Augmented Generation (RAG)** pipeline with a summary of the book, includes a **Memory Advisor** to personalize responses, and functions as an **MCP client** connecting to an MCP server via **Server-Sent Events (SSE)**.

## 📌 Features
- **Expert Q&amp;A** from *Deep Nutrition*
- **RAG** for accurate and context-rich answers
- **Meal &amp; Food Tracking** via MCP Server tools:
  - Add Users
  - Add Foods
  - Add Meals
  - Track calories, carbs, fats, proteins over a given period
  - Query remaining calories/macros
  - Count specific food consumption
  - Track harmful oils consumption
- **Memory Advisor** for personalized interactions
- **Persistent SSE connection** to MCP Server for real-time tool invocation
