using System;
using System.Collections.Generic;
using System.Linq;
using System.Net.Http.Headers;
using System.Net.Http;
using System.Text;
using System.Text.Json;
using System.Threading.Tasks;
using CodeVerse.BrzoDoLokacije.Common.UserService;
using Microsoft.AspNetCore.Http;
using System.Net.Http.Json;

namespace CodeVerse.BrzoDoLokacije.Common.HttpClientHelper
{
    public class HttpClientHelper : IHttpClientHelper
    {
        public async Task<T?> GetAsync<T>(string url, Dictionary<string, string>? headers = null)
        {
            using (HttpClient httpClient = new HttpClient() { BaseAddress = new Uri(url) })
            {
                if (headers != null)
                {
                    foreach (var header in headers)
                    {
                        httpClient.DefaultRequestHeaders.Add(header.Key, header.Value);
                    }
                }

                HttpResponseMessage response = await httpClient.GetAsync(url);
                response.EnsureSuccessStatusCode();

                var serializeOptions = new JsonSerializerOptions
                {
                    PropertyNameCaseInsensitive = true
                };

                var jsonResponse = JsonSerializer.Deserialize<T>(await response.Content.ReadAsStringAsync(), serializeOptions);
                return jsonResponse;
            }
        }

        public async Task<T?> PostAsync<T>(string url, object data, Dictionary<string, string>? headers = null) where T : new()
        {
            using (HttpClient httpClient = new HttpClient())
            {
                if (headers != null)
                {
                    foreach (var header in headers)
                    {
                        httpClient.DefaultRequestHeaders.Add(header.Key, header.Value);
                    }
                }

                using StringContent jsonContent = new(JsonSerializer.Serialize(data), Encoding.UTF8, "application/json");

                using HttpResponseMessage response = await httpClient.PostAsync(url, jsonContent);

                response.EnsureSuccessStatusCode();

                var serializeOptions = new JsonSerializerOptions
                {
                    PropertyNameCaseInsensitive = true
                };

                var jsonResponse = JsonSerializer.Deserialize<T>(await response.Content.ReadAsStringAsync(), serializeOptions);
                return jsonResponse;
            }
        }

        public async Task<T?> PostFileAsync<T>(string url, Dictionary<string, byte[]> bytes, Dictionary<string, string>? headers = null)
        {
            using (var httpClient = new HttpClient()) 
            {
                if (headers != null)
                {
                    foreach (var header in headers)
                    {
                        httpClient.DefaultRequestHeaders.Add(header.Key, header.Value);
                    }
                }

                using (var formData = new MultipartFormDataContent())
                {
                    foreach (var b in bytes)
                    {
                        HttpContent httpContent = new ByteArrayContent(b.Value);
                        formData.Add(httpContent, "files", b.Key);
                    }

                    using HttpResponseMessage response = await httpClient.PostAsync(url, formData);

                    response.EnsureSuccessStatusCode();

                    var serializeOptions = new JsonSerializerOptions
                    {
                        PropertyNameCaseInsensitive = true
                    };

                    var jsonResponse = JsonSerializer.Deserialize<T>(await response.Content.ReadAsStringAsync(), serializeOptions);
                    return jsonResponse;
                }
            }
        }
    }
}
