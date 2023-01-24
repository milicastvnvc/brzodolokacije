using System;
using System.Collections.Generic;
using System.Linq;
using System.Net.Http.Headers;
using System.Text;
using System.Threading.Tasks;

namespace CodeVerse.BrzoDoLokacije.Common.HttpClientHelper
{
    public interface IHttpClientHelper
    {
        Task<T?> GetAsync<T>(string url, Dictionary<string, string>? headers = null);
        Task<T?> PostAsync<T>(string url, object data, Dictionary<string, string>? headers = null) where T : new();
        Task<T?> PostFileAsync<T>(string url, Dictionary<string, byte[]> bytes, Dictionary<string, string>? headers = null);
    }
}