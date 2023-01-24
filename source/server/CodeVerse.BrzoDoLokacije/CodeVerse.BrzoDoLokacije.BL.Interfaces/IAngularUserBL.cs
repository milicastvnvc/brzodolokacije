using CodeVerse.BrzoDoLokacije.Models.ViewModel;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CodeVerse.BrzoDoLokacije.BL.Interfaces
{
    public interface IAngularUserBL
    {
        Task<ActionResultResponse> AddAngularUser(AddAngularUserRequest request);
        Task<ActionResultResponse> LoginAngularUser(AddAngularUserRequest request);
        Task<byte[]?> GetApplication();
        Task AddApplication(byte[] application);
    }
}