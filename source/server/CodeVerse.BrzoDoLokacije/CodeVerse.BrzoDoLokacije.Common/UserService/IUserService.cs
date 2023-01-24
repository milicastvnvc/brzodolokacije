using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CodeVerse.BrzoDoLokacije.Common.UserService
{
    public interface IUserService
    {
        string? GetUserToken();
        long? GetUserId();
    }
}
