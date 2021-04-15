package charChem.lang

val baseDictRu: Map<String, String> = mapOf(
    "\$Native" to "Русский", "\$English" to "Russian",
    // Ошибки
    "Internal error: [msg]" to "Внутренняя ошибка: [msg]",
    "Formula can not be displayed as text" to
            "Формулу нельзя отобразить в текстовом виде",
    "Expected '(' after [S]" to " Требуется '(' после [S]",
    "Unexpected '[C]'" to "Неверный символ '[C]' в позиции [pos]",
    "Expected '[ok]' instead of '[bad]'" to
            "Требуется '[ok]' вместо '[bad]' в позиции [pos]",
    "Invalid character '[C]'" to
            "Недопустимый символ '[C]' в позиции [pos]",
    "Russian element character" to // param: C
            "Недопустимый русский символ '[C]'. Для описания химического элемента должны использоваться только латинские символы.",
    "Non-latin element character" to // param: C
            "Недопустимый символ '[C]'. Для описания химического элемента должны использоваться только латинские символы.",
    "Unknown element character '[C]'" to
            "Недопустимый символ '[C]' описания реагента в позиции [pos]",
    "Expected '[C]'" to "Требуется '[C]' в позиции [pos]",
    "Unknown element '[Elem]'" to "Ошибочный элемент '[Elem]' в позиции [pos]",
    "Comment is not closed" to "Не закрыт комментарий, начатый в позиции [pos]",
    "Abstract coefficient is not closed" to
            "Не закрыт абстрактный коэффициент, начатый в позиции [pos]",
    "Abstract element is not closed" to
            "Не закрыт абстрактный элемент, начатый в позиции [pos]",
    "Expected node declaration before charge" to
            "Неизвестно, к чему нужно применить заряд в позиции [pos]",
    "Invalid charge declaration" to
            "Ошибка в описании заряда в позиции [pos]",
    "It is necessary to close the bracket" to
            "Необходимо закрыть скобку, открытую в позиции [pos]",
    "Undefined variable [name]" to
            "Не определена числовая переменная '[name]' в позиции [pos]",
    "Invalid node reference '[ref]'" to
            "Неправильная ссылка на узел '[ref]' в позиции [pos]",
    "Invalid label" to
            "Неправильная метка в позиции [pos]",
    "Invalid branch close" to
            "Нельзя закрыть ветку в позиции [pos], которая не открыта",
    "Cant close branch before bracket" to
            "Нельзя закрыть ветку в позиции [pos], пока не закрыта скобка в позиции [pos0]",
    "Invalid bracket close" to
            "Нет пары для скобки, закрытой в позиции [pos]",
    "It is necessary to close the branch" to
            "Необходимо закрыть ветку, открытую в позиции [pos]",
    "Expected [must] instead of [have]" to
            "Требуется [must] вместо [have] в позиции [pos]",
    "Invalid middle point" to
            "Не используется промежуточная точка",
    "Cant create ring" to "Невозможно создать кольцо",
    "Cant close ring" to "Невозможно замкнуть кольцо",
    "Invalid version" to "Для формулы требуется CharChem версии [need] вместо [cur]",
        "Invalid number [n]" to "Неверное числовое значение [n] в позиции [pos]",

    "(s)" to "(тв)", "(l)" to "(ж)", "(g)" to "(г)", "(aq)" to "(р-р)",
    "H" to "Водород", "He" to "Гелий", "Li" to "Литий", "Be" to "Бериллий", "B" to "Бор", "C" to "Углерод",
    "N" to "Азот", "O" to "Кислород", "F" to "Фтор", "Ne" to "Неон", "Na" to "Натрий", "Mg" to "Магний",
    "Al" to "Алюминий", "Si" to "Кремний", "P" to "Фосфор", "S" to "Сера", "Cl" to "Хлор", "Ar" to "Аргон",
    "K" to "Калий", "Ca" to "Кальций", "Sc" to "Скандий", "Ti" to "Титан", "V" to "Ванадий", "Cr" to "Хром",
    "Mn" to "Марганец", "Fe" to "Железо", "Co" to "Кобальт", "Ni" to "Никель", "Cu" to "Медь", "Zn" to "Цинк",
    "Ga" to "Галлий", "Ge" to "Германий", "As" to "Мышьяк", "Se" to "Селен", "Br" to "Бром", "Kr" to "Криптон",
    "Rb" to "Рубидий", "Sr" to "Стронций", "Y" to "Иттрий", "Zr" to "Цирконий", "Nb" to "Ниобий", "Mo" to "Молибден",
    "Tc" to "Технеций", "Ru" to "Рутений", "Rh" to "Родий", "Pd" to "Палладий", "Ag" to "Серебро", "Cd" to "Кадмий",
    "In" to "Индий", "Sn" to "Олово", "Sb" to "Сурьма", "Te" to "Теллур", "I" to "Йод", "Xe" to "Ксенон",
    "Cs" to "Цезий", "Ba" to "Барий", "La" to "Лантан", "Ce" to "Церий", "Pr" to "Празеодим", "Nd" to "Неодим",
    "Pm" to "Прометий", "Sm" to "Самарий", "Eu" to "Европий", "Gd" to "Гадолиний", "Tb" to "Тербий",
    "Dy" to "Диспрозий", "Ho" to "Гольмий", "Er" to "Эрбий", "Tm" to "Тулий", "Yb" to "Иттербий", "Lu" to "Лютеций",
    "Hf" to "Гафний", "Ta" to "Тантал", "W" to "Вольфрам", "Re" to "Рений", "Os" to "Осмий", "Ir" to "Иридий",
    "Pt" to "Платина", "Au" to "Золото", "Hg" to "Ртуть", "Tl" to "Таллий", "Pb" to "Свинец", "Bi" to "Висмут",
    "Po" to "Полоний", "At" to "Астат", "Rn" to "Радон", "Fr" to "Франций", "Ra" to "Радий", "Ac" to "Актиний",
    "Th" to "Торий", "Pa" to "Протактиний", "U" to "Уран", "Np" to "Нептуний", "Pu" to "Плутоний", "Am" to "Америций",
    "Cm" to "Кюрий", "Bk" to "Берклий", "Cf" to "Калифорний", "Es" to "Эйнштейний", "Fm" to "Фермий",
    "Md" to "Менделеевий", "No" to "Нобелий", "Lr" to "Лоуренсий", "Rf" to "Резерфордий", "Db" to "Дубний",
    "Sg" to "Сиборгий", "Bh" to "Борий", "Hs" to "Хассий", "Mt" to "Мейтнерий", "Ds" to "Дармштадтий",
    "Rg" to "Рентгений", "Cn" to "Коперниций", "Nh" to "Нихоний", "Fl" to "Флеровий", "Mc" to "Московий",
    "Lv" to "Ливерморий", "Ts" to "Теннессин", "Og" to "Оганесон",
)