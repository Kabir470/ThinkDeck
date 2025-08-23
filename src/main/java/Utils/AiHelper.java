package Utils;

import create_flashcard.Flashcard;
import create_quiz.AiQuizQuestion;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class AiHelper {

    // TODO: Replace with your actual API key
    private static final String API_KEY = "UR_API_KEY";
    private static final String API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash-latest:generateContent?key="
            + API_KEY;

    private static String callGeminiApi(String prompt) {
        try {
            URL url = new URL(API_URL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            // Create the JSON payload using JSONObject and JSONArray
            JSONObject textPart = new JSONObject();
            textPart.put("text", prompt);

            JSONArray partsArray = new JSONArray();
            partsArray.put(textPart);

            JSONObject content = new JSONObject();
            content.put("parts", partsArray);

            JSONArray contentsArray = new JSONArray();
            contentsArray.put(content);

            JSONObject requestBodyJson = new JSONObject();
            requestBodyJson.put("contents", contentsArray);

            String requestBody = requestBodyJson.toString();

            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = requestBody.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                try (BufferedReader br = new BufferedReader(
                        new InputStreamReader(connection.getInputStream(), "utf-8"))) {
                    StringBuilder response = new StringBuilder();
                    String responseLine;
                    while ((responseLine = br.readLine()) != null) {
                        response.append(responseLine.trim());
                    }
                    return response.toString();
                }
            } else {
                try (BufferedReader br = new BufferedReader(
                        new InputStreamReader(connection.getErrorStream(), "utf-8"))) {
                    StringBuilder errorResponse = new StringBuilder();
                    String responseLine;
                    while ((responseLine = br.readLine()) != null) {
                        errorResponse.append(responseLine.trim());
                    }
                    System.err.println("Error response from API: " + errorResponse.toString());
                }
                return "Error: " + responseCode + " " + connection.getResponseMessage();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Error: " + e.getMessage();
        }
    }

    public static List<Flashcard> generateFlashcards(String topic) {
        List<Flashcard> flashcards = new ArrayList<>();
        String prompt = "Generate 5 flashcards for the topic '" + topic
                + "'. Each flashcard should have a question and a short answer. Format the output as a JSON array where each object has 'question' and 'answer' keys.";

        String jsonResponse = callGeminiApi(prompt);

        try {
            JSONObject responseJson = new JSONObject(jsonResponse);
            JSONArray candidates = responseJson.getJSONArray("candidates");
            if (candidates.length() > 0) {
                JSONObject firstCandidate = candidates.getJSONObject(0);
                JSONObject content = firstCandidate.getJSONObject("content");
                JSONArray parts = content.getJSONArray("parts");
                if (parts.length() > 0) {
                    String text = parts.getJSONObject(0).getString("text");
                    // Clean the text to be valid JSON
                    text = text.replaceAll("```json", "").replaceAll("```", "").trim();
                    JSONArray generatedCards = new JSONArray(text);
                    for (int i = 0; i < generatedCards.length(); i++) {
                        JSONObject cardJson = generatedCards.getJSONObject(i);
                        String question = cardJson.getString("question");
                        String answer = cardJson.getString("answer");
                        flashcards.add(new Flashcard(question, answer));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return flashcards;
    }

    public static String getAnswer(String question) {
        String prompt = "Answer the following question concisely: " + question;
        String jsonResponse = callGeminiApi(prompt);

        try {
            JSONObject responseJson = new JSONObject(jsonResponse);
            JSONArray candidates = responseJson.getJSONArray("candidates");
            if (candidates.length() > 0) {
                JSONObject firstCandidate = candidates.getJSONObject(0);
                JSONObject content = firstCandidate.getJSONObject("content");
                JSONArray parts = content.getJSONArray("parts");
                if (parts.length() > 0) {
                    return parts.getJSONObject(0).getString("text");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "I'm sorry, I could not get an answer from the AI.";
    }

    public static List<AiQuizQuestion> generateQuiz(String topic) {
        List<AiQuizQuestion> quizQuestions = new ArrayList<>();
        String prompt = "Generate a 5-question multiple-choice quiz on the topic '" + topic + "'. " +
                "For each question, provide a question, 4 options, and the index of the correct option. " +
                "Format the output as a JSON array where each object has 'question' (string), 'options' (array of 4 strings), and 'correctOptionIndex' (integer 0-3) keys.";

        String jsonResponse = callGeminiApi(prompt);

        try {
            JSONObject responseJson = new JSONObject(jsonResponse);
            JSONArray candidates = responseJson.getJSONArray("candidates");
            if (candidates.length() > 0) {
                JSONObject firstCandidate = candidates.getJSONObject(0);
                JSONObject content = firstCandidate.getJSONObject("content");
                JSONArray parts = content.getJSONArray("parts");
                if (parts.length() > 0) {
                    String text = parts.getJSONObject(0).getString("text");
                    text = text.replaceAll("```json", "").replaceAll("```", "").trim();
                    JSONArray generatedQuestions = new JSONArray(text);
                    for (int i = 0; i < generatedQuestions.length(); i++) {
                        JSONObject qJson = generatedQuestions.getJSONObject(i);
                        String question = qJson.getString("question");
                        JSONArray optionsJson = qJson.getJSONArray("options");
                        List<String> options = new ArrayList<>();
                        for (int j = 0; j < optionsJson.length(); j++) {
                            options.add(optionsJson.getString(j));
                        }
                        int correctOptionIndex = qJson.getInt("correctOptionIndex");
                        quizQuestions.add(new AiQuizQuestion(question, options, correctOptionIndex));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return quizQuestions;
    }
}
