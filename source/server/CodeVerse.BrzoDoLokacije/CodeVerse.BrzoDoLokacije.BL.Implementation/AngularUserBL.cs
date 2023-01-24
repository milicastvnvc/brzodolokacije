using CodeVerse.BrzoDoLokacije.BL.Interfaces;
using CodeVerse.BrzoDoLokacije.DAL.Interfaces;
using CodeVerse.BrzoDoLokacije.Models.Entity;
using CodeVerse.BrzoDoLokacije.Models.ViewModel;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CodeVerse.BrzoDoLokacije.BL.Implementation
{
    public class AngularUserBL : IAngularUserBL
    {
        private readonly IAngularUserDAL _angularUserDAL;

        public AngularUserBL(IAngularUserDAL angularUserDAL)
        {
            _angularUserDAL = angularUserDAL;
        }

        public async Task<ActionResultResponse> AddAngularUser(AddAngularUserRequest request)
        {
            ActionResultResponse actionResult = new ActionResultResponse();
            var userExists = await _angularUserDAL.GetByUsername(request.Username);

            if (userExists != null)
            {
                actionResult.SetFalseSucces($"Korisnik sa korisnickim imenom {request.Username} postoji!");
                return actionResult;
            }

            AngularUser user = new AngularUser();
            user.Username = request.Username;
            user.Password = BCrypt.Net.BCrypt.HashPassword(request.Password);

            await _angularUserDAL.Insert(user);

            actionResult.Success = true;
            return actionResult;
        }

        public async Task AddApplication(byte[] application)
        {
            await _angularUserDAL.AddApplication(application);
        }

        public async Task<byte[]?> GetApplication()
        {
            return await _angularUserDAL.GetApplication();
        }

        public async Task<ActionResultResponse> LoginAngularUser(AddAngularUserRequest request)
        {
            ActionResultResponse actionResult = new ActionResultResponse();

            var userExists = await _angularUserDAL.GetByUsername(request.Username);

            if (userExists == null)
            {
                actionResult.SetFalseSucces($"Korisnicko ime ne postoji!");
                return actionResult;
            }

            var passwordVerified = BCrypt.Net.BCrypt.Verify(request.Password, userExists.Password);

            if (!passwordVerified)
            {
                actionResult.SetFalseSucces($"Pogresna lozinka!");
                return actionResult;
            }

            var jwt = TokenHelper.TokenHelper.CreateToken(null, userExists);
            actionResult.ActionData = new { User = new { Id = userExists.Id, Username = userExists.Username }, Token = jwt };
            actionResult.Success = true;

            return actionResult;
        }
    }
}
