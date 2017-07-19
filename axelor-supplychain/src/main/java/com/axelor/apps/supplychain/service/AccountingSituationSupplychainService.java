/**
 * Axelor Business Solutions
 *
 * Copyright (C) 2017 Axelor (<http://axelor.com>).
 *
 * This program is free software: you can redistribute it and/or  modify
 * it under the terms of the GNU Affero General Public License, version 3,
 * as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.axelor.apps.supplychain.service;

import com.axelor.apps.account.db.AccountingSituation;
import com.axelor.apps.account.service.AccountingSituationService;
import com.axelor.apps.base.db.Company;
import com.axelor.apps.base.db.Partner;
import com.axelor.apps.sale.db.SaleOrder;
import com.axelor.exception.AxelorException;

public interface AccountingSituationSupplychainService extends AccountingSituationService {
	
	public void updateCustomerCreditFromSaleOrder(SaleOrder saleOrder) throws AxelorException;
	
	public boolean checkBlockedPartner(Partner partner, Company company) throws AxelorException;
	
	public AccountingSituation computeUsedCredit(AccountingSituation accountingSituation);
	
	public void updateUsedCredit(Partner partner) throws AxelorException;
}