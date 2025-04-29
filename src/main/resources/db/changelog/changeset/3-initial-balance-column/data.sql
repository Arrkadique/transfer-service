alter table account add column initial_balance decimal;

update account
set initial_balance = balance;


