using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CodeVerse.BrzoDoLokacije.Models.ViewModel
{
    public class ActionResultResponse
    {
        public bool Success { get; set; }
        public List<string> Errors { get; set; } = new List<string>();
        public object ActionData { get; set; }
        public bool HasErrors => Errors != null && Errors.Any();

        public ActionResultResponse() { }

        public ActionResultResponse(string error) 
        {
            Errors.Add(error);
            Success = false;
        }

        public void AddError(string error)
        {
            Errors.Add(error);
        }

        public void SetFalseSucces(string? error = null)
        {
            Success = false;
            if (!string.IsNullOrEmpty(error))
            {
                Errors.Add(error);
            }
        }
    }
}
