//
//  AccountListViewController.m
//  BMSQS
//
//  Created by gh on 16/2/23.
//  Copyright © 2016年 djx. All rights reserved.
//

#import "AccountListViewController.h"
#import "AccountListCell.h"
#import "MJRefresh.h"
#import "AccountDetailViewController.h"
#import "SVProgressHUD.h"


@interface AccountListViewController () <UITableViewDataSource, UITableViewDelegate, UISearchBarDelegate> {
    int page;
    NSString *timeLimit;
    NSString *billType;
    NSString *searchWordStr;
    UIView *bottomView;
    
    CGFloat topViewHeight; // 顶部视图
    CGFloat tableViewY;
}

@property (nonatomic, strong)UISearchBar *tx_search;
@property (nonatomic, strong)NSMutableArray *accountData;
@property (nonatomic, strong)UITableView *tableView;
//@property (nonatomic, strong)UI



@end

@implementation AccountListViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    
    
    [self setViewUp];
}

//1000 账单查询 1100 顾客清单 1101 消费未结算 1102 支付结算对账 1200 退款清单 1201 补贴未结算账单 1202 补贴结算对账
- (void)setViewUp {
    
    self.view.backgroundColor = UICOLOR(239, 238, 246, 1.0);
    timeLimit = @"1";
    searchWordStr = @"";
    self.accountData = [[NSMutableArray alloc] init];
    [self setNavBackItem];
    [self setViewTitle];
    
    topViewHeight = 40;
    if (_vcTag == 1000) {
        topViewHeight = 80;
    }
    tableViewY = topViewHeight+5;
    
    self.tableView = [[UITableView alloc] initWithFrame:CGRectMake(0, APP_VIEW_ORIGIN_Y+tableViewY, APP_VIEW_WIDTH, APP_VIEW_HEIGHT-APP_VIEW_ORIGIN_Y-tableViewY)];
    self.tableView.separatorStyle = UITableViewCellSeparatorStyleNone;
    self.tableView.backgroundColor = [UIColor clearColor];
    self.tableView.dataSource = self;
    self.tableView.delegate = self;
    
    [self.view addSubview:self.tableView];
   
    [self setTopView];
    
}


- (void)footerRereshing {
    page ++;
    
    [self getBillList];
    
}


#pragma mark - UITableView delegate
- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {

    if (!self.accountData) {
        return 0;
    }else {
        return self.accountData.count;
    }
}



- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    
    NSString *cellIdentify = @"accountCell";
    
    AccountListCell *cell = (AccountListCell *)[tableView dequeueReusableCellWithIdentifier:cellIdentify];
    if (!cell) {
        cell = [[AccountListCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:cellIdentify];
    }
    cell.selectionStyle = UITableViewCellSelectionStyleNone;
    if (self.accountData) {
        NSDictionary *dic = [self.accountData objectAtIndex:indexPath.row] ;
        [cell configUI:_vcTag value:dic];
    }
    
    return cell;
    
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath {
    
    if (_vcTag == 1100) { //顾客清单
        return 81;
    } else if (_vcTag == 1200) { //退款清单
        return 121;
    } else if (_vcTag == 1201 || _vcTag == 1000 || _vcTag == 1202) {
        return 101;
    }
    
    return 81;
    
}


- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
    if (_vcTag == 1100) {
        return;
    }
    NSDictionary *dic = [self.accountData objectAtIndex:indexPath.row];
    NSLog(@"%@",dic);
    AccountDetailViewController *vc = [[AccountDetailViewController alloc] init];
    vc.consumeCode = [dic objectForKey:@"consumeCode"];
    [self.navigationController pushViewController:vc animated:YES];
    
}



//创建顶部视图
- (void)setTopView {
    UIView *view = [[UIView alloc] initWithFrame:CGRectMake(0, APP_VIEW_ORIGIN_Y, APP_VIEW_WIDTH, topViewHeight)];
    view.backgroundColor = [UIColor whiteColor];
    [self.view addSubview:view];
    //button
    if (_vcTag == 1100 || _vcTag == 1200 || _vcTag == 1101 ||  _vcTag == 1201) {
        [self setTopButton:view isQuery:NO];
    
        //竖线
        for (int i=1; i<4; i++) {
            [self ggsetLineView:CGRectMake(APP_VIEW_WIDTH/4 * i, 0, 1, 40) view:view];
        }
        [self ggsetLineView:CGRectMake(0, 39, APP_VIEW_WIDTH, 1) view:view];

    } else if (_vcTag == 1102 || _vcTag == 1202) {
        //清算日期 和 平台发起的清算资金
        for (int i=0; i<2; i++) {
            UILabel *label = [[UILabel alloc] initWithFrame:CGRectMake(10, 20*i, APP_VIEW_WIDTH-20, 20)];
            label.font = [UIFont systemFontOfSize:13.f];
            label.textColor = UICOLOR(101, 102, 103, 1.0);
            label.tag = 20000+i;
//            label.text = @"清算日期";
            [view addSubview:label];
        }
        
    } else if (_vcTag == 1000) { //查询
        
        
        UITextField *queryText = [[UITextField alloc] initWithFrame:CGRectMake(10, 0, APP_VIEW_WIDTH-20-80, 40)];
        queryText.tag = 4001;
        queryText.font = [UIFont systemFontOfSize:13.f];
        queryText.keyboardType = UIKeyboardTypeDecimalPad;
        queryText.placeholder = @"请输入手机号/订单号";
        queryText.clearButtonMode = UITextFieldViewModeAlways;
        [view addSubview:queryText];
        /*
        self.tx_search = [[UISearchBar alloc]initWithFrame:CGRectMake(5, 5, APP_VIEW_WIDTH - 60, 30)];
        self.tx_search.delegate = self;
        self.tx_search.returnKeyType = UIReturnKeySearch;
        [self.tx_search setPlaceholder:@"请输入订单号/手机号/餐号"];
//        self.tx_search.text = self.searchStr;
        
        
        [view addSubview:self.tx_search];
        UITextField *searchField = [self.tx_search valueForKey:@"_searchField"];
        searchField.backgroundColor = self.view.backgroundColor;
        searchField.font = [UIFont systemFontOfSize:13.f];
        
        UIButton *searchBtn = [[UIButton alloc]initWithFrame:CGRectMake(self.tx_search.frame.size.width, 0, 40, 40)];
        [searchBtn setTitle:@"查询" forState:UIControlStateNormal];
        [searchBtn setTitleColor:APP_TEXTCOLOR forState:UIControlStateNormal];
        searchBtn.titleLabel.font =[UIFont systemFontOfSize:14];
        [searchBtn addTarget:self action:@selector(queryBtnAct:) forControlEvents:UIControlEventTouchUpInside];
        [view addSubview:searchBtn];
        //设置背景图片
        float version = [[[ UIDevice currentDevice ] systemVersion ] floatValue ];
        
        if ([ self.tx_search respondsToSelector : @selector (barTintColor)]) {
            
            float  iosversion7_1 = 7.1 ;
            if (version >= iosversion7_1)
                
            {
                //iOS7.1
                [[[[ self.tx_search . subviews objectAtIndex : 0 ] subviews ] objectAtIndex : 0 ] removeFromSuperview ];
                [ self.tx_search setBackgroundColor :[ UIColor clearColor ]];
            }
            else
            {
                //iOS7.0
                [ self.tx_search setBarTintColor :[ UIColor clearColor ]];
                [ self.tx_search setBackgroundColor :[ UIColor clearColor ]];
                
            }
        }
        else
        {
            //iOS7.0 以下
            [[ self.tx_search . subviews objectAtIndex : 0 ] removeFromSuperview ];
            [ self.tx_search setBackgroundColor :[ UIColor clearColor ]];
            
        }
        
        */
        UIButton *queryButton = [UIButton buttonWithType:UIButtonTypeCustom];
        [queryButton setTitle:@"查询" forState:UIControlStateNormal];
        [queryButton.titleLabel setFont:[UIFont systemFontOfSize:13.0]];
        [queryButton setTitleColor:UICOLOR(19, 22, 26, 1.0) forState:UIControlStateNormal];
        queryButton.frame = CGRectMake(APP_VIEW_WIDTH-80, 5, 70, 30);
        [queryButton addTarget:self action:@selector(queryBtnAct:) forControlEvents:UIControlEventTouchUpInside];
        [view addSubview:queryButton];
        
        
        [self setTopButton:view isQuery:YES];
        //竖线
        [self ggsetLineView:CGRectMake(0, 40, APP_VIEW_WIDTH, 1) view:view];
        for (int i=1; i<4; i++) {
            [self ggsetLineView:CGRectMake(APP_VIEW_WIDTH/4 * i, 40, 1, 40) view:view];
        }
        [self ggsetLineView:CGRectMake(0, 79, APP_VIEW_WIDTH, 1) view:view];
        
        
    }
    
}
//1	消费次数	consumptionNbr	N11	Y
//2	消费金额	consumptionAmount	N11,2	Y	单位：元
//3	支付金额	payAmount	N11,2	Y	单位：元
//4	优惠金额	discountAmount	N11,2	Y	单位：元
//5	退款金额	refundAmount	N11,2	Y	单位：元

//创建底部视图
- (void)setBottomView:(NSDictionary *)dic {
    
    if (!bottomView) {
        [self setBottomLabel];
    }
    self.tableView.frame = CGRectMake(0, APP_VIEW_ORIGIN_Y+tableViewY, APP_VIEW_WIDTH, APP_VIEW_HEIGHT-APP_VIEW_ORIGIN_Y - tableViewY - bottomView.frame.size.height);
    
    UILabel *topLabel0 = [bottomView viewWithTag:2000];
    UILabel *topLabel1 = [bottomView viewWithTag:2001];
    UILabel *bottomLabel0 = [bottomView viewWithTag:3000];
    UILabel *bottomLabel1 = [bottomView viewWithTag:3001];
    if (_vcTag == 1100) { //顾客清单
        topLabel0.text = [NSString stringWithFormat:@"消费%@次",[dic objectForKey:@"consumptionNbr"]];
        topLabel1.text = [NSString stringWithFormat:@"消费%.2f元", [[dic objectForKey:@"consumptionAmount"] floatValue]];
        bottomLabel0.text = [NSString stringWithFormat:@"支付%.2f元",[[dic objectForKey:@"payAmount"] floatValue]];
        bottomLabel1.text = [NSString stringWithFormat:@"优惠%.2f元",[[dic objectForKey:@"discountAmount"] floatValue]];
        
    } else if (_vcTag == 1101) { //消费未结算
        topLabel0.text = [NSString stringWithFormat:@"消费:%.2f元",[[dic objectForKey:@"consumptionAmount"] floatValue]];
        
    } else if (_vcTag == 1201) {
        topLabel0.text = [NSString stringWithFormat:@"消费:%.2f元",[[dic objectForKey:@"consumptionAmount"] floatValue]];
        topLabel1.text = [NSString stringWithFormat:@"补贴%.2f元", [[dic objectForKey:@"subsidyAmount"] floatValue]];
    } else if (_vcTag == 1200) { //退款清单
        topLabel0.text =[NSString stringWithFormat:@"消费%.2f元",[[dic objectForKey:@"consumptionAmount"] floatValue]];
        topLabel1.text =[NSString stringWithFormat:@"退款%.2f元",[[dic objectForKey:@"refundAmount"] floatValue]] ;
    } else if (_vcTag == 1102) {//支付结算对账
        topLabel0.text = [NSString stringWithFormat:@"合计%@次", [dic objectForKey:@"consumptionNbr"]];
        topLabel1.text = [NSString stringWithFormat:@"消费%.2f元", [[dic objectForKey:@"consumptionAmount"] floatValue]];
        bottomLabel0.text = [NSString stringWithFormat:@"支付%.2f元", [[dic objectForKey:@"payAmount"] floatValue]];
        
        UILabel *label1 = [self.view viewWithTag:20000];
        label1.text = [NSString stringWithFormat:@"清算日期:%@至%@",[dic objectForKey:@"startDate"], [dic objectForKey:@"endDate"]];
        UILabel *label2 = [self.view viewWithTag:20001];
        label2.text = [NSString stringWithFormat:@"平台发起的清算资金%.2f", [[dic objectForKey:@"payAmount"] floatValue]];
    } else if (_vcTag == 1202) {//补贴结算对账

        topLabel0.text = [NSString stringWithFormat:@"合计%@次", [dic objectForKey:@"consumptionNbr"]];
        topLabel1.text = [NSString stringWithFormat:@"消费%.2f元", [[dic objectForKey:@"consumptionAmount"] floatValue]];
        bottomLabel0.text = [NSString stringWithFormat:@"支付%.2f元", [[dic objectForKey:@"payAmount"] floatValue]];
        bottomLabel1.text = [NSString stringWithFormat:@"补贴%.2f元", [[dic objectForKey:@"subsidyAmount"] floatValue]];
        
        UILabel *label1 = [self.view viewWithTag:20000];
        label1.text = [NSString stringWithFormat:@"补贴日期:%@至%@",[dic objectForKey:@"startDate"], [dic objectForKey:@"endDate"]];
        UILabel *label2 = [self.view viewWithTag:20001];
        label2.text = [NSString stringWithFormat:@"平台发起的补贴资金%.2f", [[dic objectForKey:@"subsidyAmount"] floatValue]];
    } else if (_vcTag == 1000) {
        topLabel0.text = [NSString stringWithFormat:@"合计%@次", [dic objectForKey:@"consumptionNbr"]];
        topLabel1.text = [NSString stringWithFormat:@"消费%.2f元", [[dic objectForKey:@"consumptionAmount"] floatValue]];
        bottomLabel0.text = [NSString stringWithFormat:@"支付%.2f元", [[dic objectForKey:@"payAmount"] floatValue]];
        
        
    }
    
    
}

- (void)setBottomLabel {
    CGFloat viewHeight = 20;
    if (_vcTag == 1100 || _vcTag == 1102 || _vcTag == 1202 || _vcTag == 1000) {
        viewHeight = 40;
    }
    
    bottomView = [[UIView alloc] initWithFrame:CGRectMake(0, APP_VIEW_HEIGHT-viewHeight, APP_VIEW_WIDTH, viewHeight)];
    bottomView.backgroundColor = [UIColor whiteColor];
    [self.view addSubview:bottomView];
    
    [self ggsetLineView:CGRectMake(0, 0, APP_VIEW_WIDTH, 0.5) view:bottomView];
    
    CGFloat bottomLabelY = 0;
    for (int i=0; i<2; i++) {
        
        for (int j=0; j<2; j++) {
            UILabel *label = [[UILabel alloc] initWithFrame:CGRectMake(10, bottomLabelY, APP_VIEW_WIDTH-10, 20)];
            label.font = [UIFont systemFontOfSize:13.f];
            [bottomView addSubview:label];
            
            if (j == 1) {
                label.frame = CGRectMake(APP_VIEW_WIDTH/2, bottomLabelY, APP_VIEW_WIDTH-10, 20);
            }
            if (i == 0) {
                label.tag = 2000+j;
            }else if (i==1) {
                label.tag = 3000+j;
            }
            
        }
        bottomLabelY = 20;
        
    }
    
    

}

//今天 最近一周 最近一月 全部
- (void)setTopButton:(UIView *)view isQuery:(BOOL)isQuery {
    
    CGFloat buttonY = 0;
    if (isQuery) {
        buttonY = 40;
    }

    NSArray *array = [NSArray arrayWithObjects:@"今天", @"最近一周", @"最近一月", @"全部", nil];
    for (int i = 0; i<4; i++) {
        UIButton *button = [self setTopSubButton:CGRectMake((APP_VIEW_WIDTH)/4 * i, buttonY, (APP_VIEW_WIDTH)/4, 40)];
        button.tag = 1000+i;
        [button setTitle:array[i] forState:UIControlStateNormal];
        [view addSubview:button];
        
        if (i == 0) {
            button.selected = YES;
        }
    }

    
}

//单个button设置
- (UIButton *)setTopSubButton:(CGRect)frame {
    UIButton *button = [UIButton buttonWithType:UIButtonTypeCustom];
    button.frame = frame ;
    [button.titleLabel setFont:[UIFont systemFontOfSize:14]];
    [button addTarget:self action:@selector(btnTopAction:) forControlEvents:UIControlEventTouchUpInside];
    [button setTitleColor:UICOLOR(50, 51, 52, 1.0) forState:UIControlStateNormal];
    [button setTitleColor:UICOLOR(195, 0, 10, 1.0) forState:UIControlStateSelected];
    button.backgroundColor = UICOLOR(254, 255, 255, 1.0);
    return button;

}



//设置标题
- (void)setViewTitle {
    NSString *title;
    switch (_vcTag) {
            
        case 1000: //账单查询
            title = @"账单查询";
            billType = @"7";
            break;
        case 1100: //顾客清单
            title = @"顾客清单";
            billType = @"1";
            break;
        case 1101: //消费未结算账单
            title = @"消费未结算账单";
            billType = @"3";
            break;
        case 1102: //支付结算对账
            title = @"支付结算对账";
            billType = @"5";
            break;
        case 1200: //退款清单
            title = @"退款清单";
            billType = @"2";
            break;
        case 1201: //补贴未结算账单
            title = @"补贴未结算账单";
            billType = @"4";
            break;
        case 1202: //补贴结算对账
            title = @"补贴结算对账";
            billType = @"6";
            break;
        default:
            break;
            
    }
    [self setNavTitle:title];
    [self accountRequest];
}


#pragma mark - UIButton
- (void)btnTopAction:(UIButton *)sender {
    
    for (int i = 0; i<4; i++) {
        UIButton *button = [self.view viewWithTag:1000+i];
        button.selected = NO;
        
        if (sender.tag == 1000+i) {
            timeLimit = [NSString stringWithFormat:@"%d",i+1];
        }
    }
    sender.selected = YES;
    
    [self.tableView addFooterWithTarget:self action:@selector(footerRereshing)];
    
    [self accountRequest];
}

- (void)queryBtnAct:(UIButton *)btn {
    NSLog(@"查询");
    UITextField *textfield = [self.view viewWithTag:4001];
    [textfield resignFirstResponder];
    
    [self accountRequest];
    
}


- (void)accountRequest {
    [self.tableView setContentOffset:CGPointMake(0,0) animated:NO];
    page = 1;
    [self getBillList];
    [self getBillStatistics];
}

#pragma mark - Http request
//顾客清单
- (void)getBillList {

    UITextField *textfield = [self.view viewWithTag:4001];
    if (textfield){
        searchWordStr = textfield.text;
    }
    
    [SVProgressHUD showWithStatus:@""];
    
    [self initJsonPrcClient:@"1"];
    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
    [params setObject:[gloabFunction getShopCode] forKey:@"shopCode"]; //shopCode
    [params setObject:[NSNumber numberWithInt:page] forKey:@"page"];
    //timeLimit 1-今天 2-最近一周 3-最近一月 4-全部
    [params setObject:timeLimit forKey:@"timeLimit"];
    //billType 1-顾客清单 2-退款清单 3-消费未结算账单 4-补贴未结算账单 5-支付结算对账 6-补贴结算对账 7-账单查询
    [params setObject:billType forKey:@"billType"];
    //搜索字段	searchWord	VA25	Y	订单号或者手机号
    
    [params setObject:searchWordStr forKey:@"searchWord"];
    NSString* vcode = [gloabFunction getSign:@"getBillList" strParams:[gloabFunction getShopCode]];
    [params setObject:vcode forKey:@"vcode"];
    [params setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
    [params setObject:[gloabFunction getTimestamp] forKey:@"reqtime"];
    
    [self.jsonPrcClient invokeMethod:@"getBillList" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
//        NSLog(@"%@",responseObject);
        [SVProgressHUD dismiss];
        [self.tableView footerEndRefreshing];
        
        if (page == 1) {
            [self.accountData removeAllObjects];
            
        }
        
        [self.accountData addObjectsFromArray:[responseObject objectForKey:@"dataList"]];
        
        if ([[responseObject objectForKey:@"totalCount"] intValue] ==  self.accountData.count) {
            [self.tableView removeFooter];
        }
        
        [self.tableView reloadData];
        
        
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        [SVProgressHUD dismiss];
        [self.tableView footerEndRefreshing];
        NSLog(@"%@",error);
        
    }];
    
}

- (void)getBillStatistics {
    
    UITextField *textfield = [self.view viewWithTag:4001];
    if (textfield) {
        searchWordStr = textfield.text;
    }

    
    [self initJsonPrcClient:@"1"];
    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
    [params setObject:[gloabFunction getShopCode] forKey:@"shopCode"]; //shopCode
    [params setObject:[NSNumber numberWithInt:page] forKey:@"page"];
    //timeLimit 1-今天 2-最近一周 3-最近一月 4-全部
    [params setObject:timeLimit forKey:@"timeLimit"];
    //billType 1-顾客清单 2-退款清单 3-消费未结算账单 4-补贴未结算账单 5-支付结算对账 6-补贴结算对账 7-账单查询
    [params setObject:billType forKey:@"billType"];
    [params setObject:searchWordStr forKey:@"searchWord"];
    NSString* vcode = [gloabFunction getSign:@"getBillStatistics" strParams:[gloabFunction getShopCode]];
    [params setObject:vcode forKey:@"vcode"];
    [params setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
    [params setObject:[gloabFunction getTimestamp] forKey:@"reqtime"];
    
    [self.jsonPrcClient invokeMethod:@"getBillStatistics" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        //        NSLog(@"%@",responseObject);
       
        if (responseObject) {
            [self setBottomView:responseObject];
            
        }
       
        
        
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        [self.tableView footerEndRefreshing];
        NSLog(@"%@",error);
        
    }];
    
}


- (void)ggsetLineView:(CGRect)frame view:(UIView *)view{
    UIView *lineView = [[UIView alloc] initWithFrame:frame];
    lineView.backgroundColor = UICOLOR(214, 215, 219, 1);
    [view addSubview:lineView];
    
}

@end
